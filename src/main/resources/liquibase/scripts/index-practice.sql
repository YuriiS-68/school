-- liquibase formatted sql

-- changeset yuvis:1
CREATE INDEX student_name_idx ON student(name);

-- changeset yuvis:2
CREATE INDEX faculty_color_name_idx ON faculty (color, name);