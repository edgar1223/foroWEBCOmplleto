import os
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.decomposition import LatentDirichletAllocation
from sklearn.preprocessing import LabelEncoder
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics.pairwise import cosine_similarity
import pickle
from sqlalchemy import create_engine

# Configurar la conexión a MySQL
engine = create_engine('mysql+pymysql://edgar:Mucikaa12$@localhost/foro')

# Consulta SQL ajustada para posts y materias
query_posts = """
SELECT 
    p.usuario_id AS student_id, 
    m.materia AS subject, 
    m.semestre AS semester,
    p.contenido AS post_content,
    p.titulo AS post_title,
    p.fecha AS post_date
FROM 
    post p
JOIN 
    materia m ON p.materia_id = m.id;
"""
df_posts = pd.read_sql(query_posts, engine)

# Consulta SQL para obtener los nombres de los profesores
query_professors = """
SELECT 
    id, nombre, apellido 
FROM 
    usuario 
WHERE 
    user_type = 'PROFESOR';
"""
df_professors = pd.read_sql(query_professors, engine)
professor_names = df_professors['nombre'] + " " + df_professors['apellido']

# Relacionar materias usando similitud en el nombre y semestre
subject_names = df_posts['subject'].unique()
subject_vectorizer = TfidfVectorizer(stop_words='english').fit(subject_names)
subject_vectors = subject_vectorizer.transform(subject_names)
subject_similarity = cosine_similarity(subject_vectors)

# Crear un DataFrame para las relaciones de similitud entre materias
subject_sim_df = pd.DataFrame(subject_similarity, index=subject_names, columns=subject_names)

# Análisis de tendencias usando LDA (Latent Dirichlet Allocation)
vectorizer = TfidfVectorizer(stop_words='english')
X = vectorizer.fit_transform(df_posts['post_content'])

lda = LatentDirichletAllocation(n_components=5, random_state=42)
lda.fit(X)

# Transformar datos completos con LDA para usar en el modelo de predicción
X_topics = lda.transform(X)

# Codificar las materias
label_encoder = LabelEncoder()
y = label_encoder.fit_transform(df_posts['subject'])

# Modelo de predicción (Random Forest)
clf = RandomForestClassifier(random_state=42)
clf.fit(X_topics, y)

# Análisis adicional: Contar nombres de profesores en los posts
professor_count = {}

def count_mentions(name, title, content):
    parts = name.split()
    combined_text = title + " " + content
    return 1 if any(part in combined_text for part in parts) else 0

for full_name in professor_names:
    count = df_posts.apply(lambda row: count_mentions(full_name, row['post_title'], row['post_content']), axis=1).sum()
    professor_count[full_name] = count

# Guardar los resultados del análisis adicional
script_dir = os.path.dirname(os.path.realpath(__file__))
with open(os.path.join(script_dir, 'professor_count.pkl'), 'wb') as f:
    pickle.dump(professor_count, f)

# Guardar los modelos y objetos necesarios en el mismo directorio que el script
with open(os.path.join(script_dir, 'vectorizer.pkl'), 'wb') as f:
    pickle.dump(vectorizer, f)
with open(os.path.join(script_dir, 'lda_model.pkl'), 'wb') as f:
    pickle.dump(lda, f)
with open(os.path.join(script_dir, 'label_encoder.pkl'), 'wb') as f:
    pickle.dump(label_encoder, f)
with open(os.path.join(script_dir, 'classifier.pkl'), 'wb') as f:
    pickle.dump(clf, f)
with open(os.path.join(script_dir, 'subject_sim_df.pkl'), 'wb') as f:
    pickle.dump(subject_sim_df, f)
with open(os.path.join(script_dir, 'df_posts.pkl'), 'wb') as f:
    pickle.dump(df_posts, f)
