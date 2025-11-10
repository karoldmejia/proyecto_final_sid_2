-- ==========================================
-- ROLES
-- ==========================================
INSERT INTO role (name, description) VALUES ('Admin', 'Administrador de la plataforma');
INSERT INTO role (name, description) VALUES ('Trainer', 'Entrenador certificado');
INSERT INTO role (name, description) VALUES ('User', 'Usuario regular');

-- ==========================================
-- PERMISSIONS
-- ==========================================

-- ====== RUTINAS ======
INSERT INTO permission (name, description) VALUES ('CREAR_RUTINA', 'Permite crear rutinas personalizadas');
INSERT INTO permission (name, description) VALUES ('EDITAR_RUTINA_PROPIA', 'Permite modificar rutinas propias');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_RUTINA_PROPIA', 'Permite eliminar rutinas propias');
INSERT INTO permission (name, description) VALUES ('VER_TODAS_RUTINAS', 'Permite ver todas las rutinas del sistema');
INSERT INTO permission (name, description) VALUES ('VER_RUTINAS_PROPIAS', 'Permite ver solo rutinas propias');
INSERT INTO permission (name, description) VALUES ('CREAR_RUTINA_CERTIFICADA', 'Permite crear rutinas certificadas prediseñadas');
INSERT INTO permission (name, description) VALUES ('EDITAR_CUALQUIER_RUTINA', 'Permite modificar cualquier rutina');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_CUALQUIER_RUTINA', 'Permite eliminar cualquier rutina');

-- ====== EJERCICIOS ======
INSERT INTO permission (name, description) VALUES ('CREAR_EJERCICIO', 'Permite crear ejercicios en el sistema');
INSERT INTO permission (name, description) VALUES ('EDITAR_EJERCICIO', 'Permite modificar ejercicios existentes');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_EJERCICIO', 'Permite eliminar ejercicios del sistema');
INSERT INTO permission (name, description) VALUES ('VER_EJERCICIOS', 'Permite consultar el catálogo de ejercicios');

-- ====== PROGRESO ======
INSERT INTO permission (name, description) VALUES ('REGISTRAR_PROGRESO_PROPIO', 'Permite registrar su propio progreso de entrenamiento');
INSERT INTO permission (name, description) VALUES ('VER_PROGRESO_PROPIO', 'Permite consultar su propio progreso');
INSERT INTO permission (name, description) VALUES ('VER_PROGRESO_USUARIOS_ASIGNADOS', 'Permite ver progreso de usuarios asignados al entrenador');
INSERT INTO permission (name, description) VALUES ('EDITAR_PROGRESO_PROPIO', 'Permite editar su propio progreso');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_PROGRESO_PROPIO', 'Permite eliminar registros de progreso propio');
INSERT INTO permission (name, description) VALUES ('VER_TODO_PROGRESO', 'Permite ver el progreso de todos los usuarios');

-- ====== EVENTOS ======
INSERT INTO permission (name, description) VALUES ('VER_EVENTOS', 'Permite consultar eventos y talleres disponibles');
INSERT INTO permission (name, description) VALUES ('INSCRIBIRSE_EVENTO', 'Permite inscribirse a eventos');
INSERT INTO permission (name, description) VALUES ('CANCELAR_INSCRIPCION_PROPIA', 'Permite cancelar inscripción propia a eventos');
INSERT INTO permission (name, description) VALUES ('CREAR_EVENTO', 'Permite crear eventos y talleres');
INSERT INTO permission (name, description) VALUES ('EDITAR_EVENTO', 'Permite modificar eventos existentes');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_EVENTO', 'Permite eliminar eventos');
INSERT INTO permission (name, description) VALUES ('MARCAR_ASISTENCIA', 'Permite marcar asistencia en eventos');
INSERT INTO permission (name, description) VALUES ('VER_INSCRIPCIONES_EVENTO', 'Permite ver inscripciones de eventos');

-- ====== ESPACIOS Y HORARIOS ======
INSERT INTO permission (name, description) VALUES ('VER_ESPACIOS', 'Permite consultar espacios disponibles');
INSERT INTO permission (name, description) VALUES ('CREAR_ESPACIO', 'Permite crear espacios');
INSERT INTO permission (name, description) VALUES ('EDITAR_ESPACIO', 'Permite modificar espacios');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_ESPACIO', 'Permite eliminar espacios');
INSERT INTO permission (name, description) VALUES ('VER_HORARIOS', 'Permite consultar horarios');
INSERT INTO permission (name, description) VALUES ('CREAR_HORARIO', 'Permite crear horarios');
INSERT INTO permission (name, description) VALUES ('EDITAR_HORARIO', 'Permite modificar horarios');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_HORARIO', 'Permite eliminar horarios');

-- ====== MENSAJERÍA ======
INSERT INTO permission (name, description) VALUES ('ENVIAR_MENSAJE', 'Permite enviar mensajes a usuarios asignados');
INSERT INTO permission (name, description) VALUES ('VER_CONVERSACIONES_PROPIAS', 'Permite ver conversaciones propias');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_MENSAJE_PROPIO', 'Permite eliminar mensajes propios');

-- ====== RECOMENDACIONES ======
INSERT INTO permission (name, description) VALUES ('CREAR_RECOMENDACION', 'Permite crear recomendaciones para usuarios');
INSERT INTO permission (name, description) VALUES ('VER_RECOMENDACIONES_PROPIAS', 'Permite ver recomendaciones recibidas');
INSERT INTO permission (name, description) VALUES ('VER_RECOMENDACIONES_CREADAS', 'Permite ver recomendaciones creadas por el entrenador');
INSERT INTO permission (name, description) VALUES ('ACTUALIZAR_ESTADO_RECOMENDACION', 'Permite actualizar estado de recomendaciones');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_RECOMENDACION', 'Permite eliminar recomendaciones');

-- ====== NOTIFICACIONES ======
INSERT INTO permission (name, description) VALUES ('VER_NOTIFICACIONES_PROPIAS', 'Permite ver notificaciones propias');
INSERT INTO permission (name, description) VALUES ('MARCAR_NOTIFICACION_LEIDA', 'Permite marcar notificaciones como leídas');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_NOTIFICACION_PROPIA', 'Permite eliminar notificaciones propias');
INSERT INTO permission (name, description) VALUES ('ENVIAR_NOTIFICACION_USUARIO', 'Permite enviar notificaciones a un usuario específico');
INSERT INTO permission (name, description) VALUES ('ENVIAR_NOTIFICACION_MASIVA', 'Permite enviar notificaciones a todos los usuarios');

-- ====== ASIGNACIONES USUARIO-ENTRENADOR ======
INSERT INTO permission (name, description) VALUES ('CREAR_ASIGNACION', 'Permite asignar entrenadores a usuarios');
INSERT INTO permission (name, description) VALUES ('VER_ASIGNACIONES_PROPIAS', 'Permite ver sus propias asignaciones');
INSERT INTO permission (name, description) VALUES ('ACTUALIZAR_ESTADO_ASIGNACION', 'Permite actualizar estado de asignaciones');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_ASIGNACION', 'Permite eliminar asignaciones');

-- ====== RELACIONES RUTINA-EJERCICIO Y USUARIO-RUTINA ======
INSERT INTO permission (name, description) VALUES ('ASIGNAR_EJERCICIO_RUTINA', 'Permite agregar ejercicios a rutinas');
INSERT INTO permission (name, description) VALUES ('ELIMINAR_EJERCICIO_RUTINA', 'Permite eliminar ejercicios de rutinas');
INSERT INTO permission (name, description) VALUES ('ASIGNAR_RUTINA_USUARIO', 'Permite asignar rutinas a usuarios');
INSERT INTO permission (name, description) VALUES ('VER_RUTINAS_ASIGNADAS', 'Permite ver rutinas asignadas');

-- ==========================================
-- ROLE_PERMISSION
-- ==========================================

-- ====== PERMISOS DEL USER ======

-- Rutinas
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'CREAR_RUTINA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'EDITAR_RUTINA_PROPIA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'ELIMINAR_RUTINA_PROPIA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_RUTINAS_PROPIAS'));

-- Ejercicios
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_EJERCICIOS'));

-- Progreso
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'REGISTRAR_PROGRESO_PROPIO'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_PROGRESO_PROPIO'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'EDITAR_PROGRESO_PROPIO'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'ELIMINAR_PROGRESO_PROPIO'));

-- Eventos
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_EVENTOS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'INSCRIBIRSE_EVENTO'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'CANCELAR_INSCRIPCION_PROPIA'));

-- Espacios y Horarios
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_ESPACIOS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_HORARIOS'));

-- Mensajería
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'ENVIAR_MENSAJE'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_CONVERSACIONES_PROPIAS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'ELIMINAR_MENSAJE_PROPIO'));

-- Recomendaciones
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_RECOMENDACIONES_PROPIAS'));

-- Notificaciones
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_NOTIFICACIONES_PROPIAS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'MARCAR_NOTIFICACION_LEIDA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'ELIMINAR_NOTIFICACION_PROPIA'));

-- Asignaciones
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_ASIGNACIONES_PROPIAS'));

-- Rutinas asignadas
INSERT INTO role_permission (role_id, permission_id) VALUES (3, (SELECT id FROM permission WHERE name = 'VER_RUTINAS_ASIGNADAS'));

-- ====== PERMISOS DEL TRAINER ======

-- Rutinas
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'CREAR_RUTINA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'CREAR_RUTINA_CERTIFICADA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'EDITAR_RUTINA_PROPIA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ELIMINAR_RUTINA_PROPIA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_TODAS_RUTINAS'));

-- Ejercicios
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_EJERCICIOS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'CREAR_EJERCICIO'));

-- Progreso
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_PROGRESO_USUARIOS_ASIGNADOS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'REGISTRAR_PROGRESO_PROPIO'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_PROGRESO_PROPIO'));

-- Eventos
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_EVENTOS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_INSCRIPCIONES_EVENTO'));

-- Espacios y Horarios
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_ESPACIOS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_HORARIOS'));

-- Mensajería
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ENVIAR_MENSAJE'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_CONVERSACIONES_PROPIAS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ELIMINAR_MENSAJE_PROPIO'));

-- Recomendaciones
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'CREAR_RECOMENDACION'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_RECOMENDACIONES_CREADAS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ACTUALIZAR_ESTADO_RECOMENDACION'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ELIMINAR_RECOMENDACION'));

-- Notificaciones
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_NOTIFICACIONES_PROPIAS'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'MARCAR_NOTIFICACION_LEIDA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ELIMINAR_NOTIFICACION_PROPIA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ENVIAR_NOTIFICACION_USUARIO'));

-- Asignaciones
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_ASIGNACIONES_PROPIAS'));

-- Relaciones
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ASIGNAR_EJERCICIO_RUTINA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ELIMINAR_EJERCICIO_RUTINA'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'ASIGNAR_RUTINA_USUARIO'));
INSERT INTO role_permission (role_id, permission_id) VALUES (2, (SELECT id FROM permission WHERE name = 'VER_RUTINAS_ASIGNADAS'));

-- ====== PERMISOS DEL ADMIN (Todos los permisos) ======
INSERT INTO role_permission (role_id, permission_id)
SELECT 1, id FROM permission;

-- ==========================================
-- USERS
-- ==========================================
INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Laura Gómez', 'laura.gomez@icesi.edu.co', '$2a$10$9QuciCPq5653XQ6UPRXZg.2JJNcnlT0xf77qJElXvxs/CJ4vn.QbC', 1); -- admin123

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Carlos Pérez', 'carlos.perez@icesi.edu.co', '$2a$10$vCio5BX5mhoOZ95P3L7KFOGql6Jxk02681SzucsJPHfhx/VgAnjci', 2); -- trainer123

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('María Torres', 'maria.torres@icesi.edu.co', '$2a$10$MkPykRXbi1wSnB8o9srD7u2x/aFiVdzk1YpVcer0B.Vp9f4r7R1P2', 2); -- trainer456

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Andrés Ríos', 'andres.rios@icesi.edu.co', '$2a$10$isF/7qudQBv.TrVsuype/.AcuW6Ixb1QMCbRyLKbeHbjlE26OCl1y', 3); -- user123

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Camila Díaz', 'camila.diaz@icesi.edu.co', '$2a$10$L5lkZ7188Z4Hv.yVPxMIOeZy0iLtWrgYABWz3uBII11.yfKakNvzS', 3); -- user456

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Juan Martínez', 'juan.martinez@icesi.edu.co', '$2a$10$cq0QO4gQ8wL0hIWTEKPjGuG3Wy1180IaSlIS0SU0QFLWQOBrUO/Hm', 3); -- user789

-- ==========================================
-- ROUTINES
-- ==========================================
INSERT INTO routine (name, creation_date, certified)
VALUES ('Rutina de Fuerza Básica', CURRENT_TIMESTAMP, true);

INSERT INTO routine (name, creation_date, certified)
VALUES ('Rutina de Cardio Ligero', CURRENT_TIMESTAMP, false);

-- ==========================================
-- EXERCISES
-- ==========================================
INSERT INTO exercise (name, type, description, duration, difficulty, video_url)
VALUES ('Sentadillas', 'Fuerza', 'Ejercicio para piernas y glúteos', 10.0, 'Media', 'https://videos.ejemplo.com/sentadillas.mp4');

INSERT INTO exercise (name, type, description, duration, difficulty, video_url)
VALUES ('Plancha', 'Resistencia', 'Fortalece el core y los brazos', 5.0, 'Alta', 'https://videos.ejemplo.com/plancha.mp4');

INSERT INTO exercise (name, type, description, duration, difficulty, video_url)
VALUES ('Caminata rápida', 'Cardio', 'Ejercicio aeróbico suave', 20.0, 'Baja', 'https://videos.ejemplo.com/caminata.mp4');

-- ==========================================
-- ROUTINE_EXERCISES
-- ==========================================
INSERT INTO routine_exercise (sets, reps, time, exercise_id, routine_id)
VALUES (3, 15, NULL, 1, 1);

INSERT INTO routine_exercise (sets, reps, time, exercise_id, routine_id)
VALUES (3, NULL, 30, 2, 1);

INSERT INTO routine_exercise (sets, reps, time, exercise_id, routine_id)
VALUES (1, NULL, 20, 3, 2);

-- ==========================================
-- USER_TRAINER_ASSIGNMENTS
-- ==========================================
INSERT INTO user_trainer_assignment (assignment_date, status, trainer_id, user_id)
VALUES (CURRENT_TIMESTAMP, 'ACTIVE', 2, 4); -- Carlos -> Andrés

INSERT INTO user_trainer_assignment (assignment_date, status, trainer_id, user_id)
VALUES (CURRENT_TIMESTAMP, 'PENDING', 3, 5); -- María -> Camila

-- ==========================================
-- USER_ROUTINES
-- ==========================================
INSERT INTO user_routine (assignment_date, status, routine_id, user_id)
VALUES (CURRENT_TIMESTAMP, true, 1, 4);

INSERT INTO user_routine (assignment_date, status, routine_id, user_id)
VALUES (CURRENT_TIMESTAMP, false, 2, 5);

-- ==========================================
-- EXERCISE_PROGRESS
-- ==========================================
INSERT INTO exercise_progress (progress_date, sets_completed, reps_completed, time_completed, effort_level, user_id, routine_exercise_id)
VALUES (CURRENT_TIMESTAMP, 3, 15, NULL, 8, 4, 1);

INSERT INTO exercise_progress (progress_date, sets_completed, reps_completed, time_completed, effort_level, user_id, routine_exercise_id)
VALUES (CURRENT_TIMESTAMP, 1, NULL, 20, 5, 5, 3);

-- ==========================================
-- RECOMMENDATIONS
-- ==========================================
INSERT INTO recommendation (recommendation_date, content, status, progress_id, trainer_id)
VALUES (CURRENT_TIMESTAMP, 'Haz 5 repeticiones extra en plancha para mejorar fuerza de core', 'NEW', 1, 2); -- Carlos -> Andrés

INSERT INTO recommendation (recommendation_date, content, status, progress_id, trainer_id)
VALUES (CURRENT_TIMESTAMP, 'Aumenta el tiempo de caminata a 25 minutos', 'NEW', 2, 3); -- María -> Camila

-- ==========================================
-- MESSAGES
-- ==========================================
INSERT INTO message (content, send_date, user_id, trainer_id)
VALUES ('Hola Andrés, recuerda hidratarte antes del entrenamiento', CURRENT_TIMESTAMP, 4, 2); -- Carlos -> Andrés

INSERT INTO message (content, send_date, user_id, trainer_id)
VALUES ('Camila, no olvides hacer los estiramientos al final', CURRENT_TIMESTAMP, 5, 3); -- María -> Camila

INSERT INTO message (content, send_date, user_id, trainer_id)
VALUES ('Andrés, me gustó tu progreso de hoy!', CURRENT_TIMESTAMP, 4, 2); -- Mensaje de usuario a entrenador

-- ==========================================
-- NOTIFICATIONS
-- ==========================================
INSERT INTO notification (origin_type, origin_id, text, sent_date, read_flag, user_id)
VALUES ('RECOMMENDATION', 1, 'Tu entrenador Carlos ha dejado una nueva recomendación.', CURRENT_TIMESTAMP, FALSE, 4);

INSERT INTO notification (origin_type, origin_id, text, sent_date, read_flag, user_id)
VALUES ('MESSAGE', 1, 'Tienes un nuevo mensaje de tu entrenador Carlos.', CURRENT_TIMESTAMP, FALSE, 4);

INSERT INTO notification (origin_type, origin_id, text, sent_date, read_flag, user_id)
VALUES ('RECOMMENDATION', 2, 'Tu entrenador María ha dejado una nueva recomendación.', CURRENT_TIMESTAMP, FALSE, 5);

INSERT INTO notification (origin_type, origin_id, text, sent_date, read_flag, user_id)
VALUES ('MESSAGE', 2, 'Tienes un nuevo mensaje de tu entrenador María.', CURRENT_TIMESTAMP, FALSE, 5);
