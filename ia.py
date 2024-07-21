import os
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.decomposition import LatentDirichletAllocation
from sklearn.preprocessing import LabelEncoder
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics.pairwise import cosine_similarity
import pickle
from sqlalchemy import create_engine
from flask import Flask, request, jsonify

# Cargar los modelos y objetos necesarios
with open('vectorizer.pkl', 'rb') as f:
    vectorizer = pickle.load(f)
with open('lda_model.pkl', 'rb') as f:
    lda = pickle.load(f)
with open('label_encoder.pkl', 'rb') as f:
    label_encoder = pickle.load(f)
with open('classifier.pkl', 'rb') as f:
    clf = pickle.load(f)
with open('subject_sim_df.pkl', 'rb') as f:
    subject_sim_df = pickle.load(f)
with open('professor_count.pkl', 'rb') as f:
    professor_count = pickle.load(f)
with open('df_posts.pkl', 'rb') as f:
    df_posts = pickle.load(f)

# Convertir los valores de professor_count a tipos nativos de Python
professor_count = {k: int(v) for k, v in professor_count.items()}

app = Flask(__name__)

@app.route('/analyze', methods=['GET'])
def analyze():
    student_id = request.args.get('student_id', None)

    if not student_id:
        return jsonify({"error": "student_id is required"}), 400

    # Filtrar datos para el usuario específico
    df_student = df_posts[df_posts['student_id'] == int(student_id)]

    if df_student.empty:
        return jsonify({"error": "No posts found for the given student_id"}), 404

    # Análisis de tendencias para el usuario específico
    X_student = vectorizer.transform(df_student['post_content'])
    student_topics = lda.transform(X_student)

    # Cuantificar número de posts del usuario por materia
    student_post_counts = df_student['subject'].value_counts()

    # Obtener el semestre más alto de las materias actuales del estudiante
    max_student_semester = df_student['semester'].max()

    # Análisis general de todos los posts
    overall_topics = lda.transform(vectorizer.transform(df_posts['post_content']))
    overall_topic_distribution = pd.DataFrame(overall_topics, columns=[f'topic_{i}' for i in range(overall_topics.shape[1])])
    overall_post_counts = df_posts['subject'].value_counts()

    # Predicción de futuras dudas para el estudiante basado en la relación entre materias
    student_future_topics = clf.predict(student_topics)
    predicted_subjects = label_encoder.inverse_transform(student_future_topics)

    # Filtrar las materias que ya ha discutido el estudiante y materias de semestres inferiores
    current_subjects = set(df_student['subject'])
    current_semesters = set(df_student['semester'])

    predicted_subjects_unique = [
        subject for subject in predicted_subjects 
        if subject not in current_subjects and 
        df_posts[df_posts['subject'] == subject]['semester'].values[0] >= max_student_semester
    ]

    # Análisis de relaciones entre materias
    similar_subjects = []
    for subject in current_subjects:
        similar_subjects.extend(subject_sim_df[subject].nlargest(6).index)

    # Predicción de futuras dudas basadas en similitud de materias y temas discutidos
    predicted_subjects_final = [
        subject for subject in similar_subjects 
        if subject not in current_subjects and 
        df_posts[df_posts['subject'] == subject]['semester'].values[0] >= max_student_semester
    ]

    # Eliminar duplicados y mantener la lista única de materias similares
    predicted_subjects_final = list(set(predicted_subjects_final))

    # Priorizar materias con más publicaciones relacionadas
    related_subjects = df_posts['subject'].value_counts().index.tolist()

    prioritized_predicted_subjects = [
        subject for subject in related_subjects
        if subject not in current_subjects and 
        df_posts[df_posts['subject'] == subject]['semester'].values[0] >= max_student_semester
    ]

    # Combinar materias priorizadas y predichas
    final_predicted_subjects = list(set(prioritized_predicted_subjects + predicted_subjects_final))

    result = {
        'relation_subjects': subject_sim_df.to_dict(),
        'overall_post_counts': overall_post_counts.to_dict(),
        'overall_topic_distributions': overall_topic_distribution.to_dict(),
        'student_post_counts': student_post_counts.to_dict(),
        'predicted_subjects': final_predicted_subjects,
        'professor_count': professor_count  # Agregar el análisis adicional de nombres de profesores
    }

    return jsonify(result)


@app.route('/professor_trend', methods=['GET'])
def professor_trend():
    return jsonify(professor_count)

@app.route('/professor_analysis', methods=['GET'])
def professor_analysis():
    return jsonify(professor_count)

@app.route('/trend_analysis', methods=['GET'])
def trend_analysis():
    student_id = request.args.get('student_id', None)

    if not student_id:
        return jsonify({"error": "student_id is required"}), 400

    df_student = df_posts[df_posts['student_id'] == int(student_id)]

    if df_student.empty:
        return jsonify({"error": "No posts found for the given student_id"}), 404

    df_student['post_date'] = pd.to_datetime(df_student['post_date'])

    # Agrupar por día y contar los posts por materia
    trend_data = df_student.groupby([df_student['post_date'].dt.to_period('D'), 'subject']).size().unstack(fill_value=0)
    trend_data.index = trend_data.index.to_timestamp()

    datasets = []
    for subject in trend_data.columns:
        data = [{'date': date.strftime('%Y-%m-%d'), 'count': count} for date, count in zip(trend_data.index, trend_data[subject])]
        datasets.append({'label': subject, 'data': data})

    result = {
        'labels': trend_data.index.strftime('%Y-%m-%d').tolist(),
        'datasets': datasets
    }

    return jsonify(result)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
