CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER CHECK ( age > 18 ),
    license BOOLEAN DEFAULT true,
    carId SERIAL REFERENCES car(id)
);

CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(70) NOT NULL,
    cost NUMERIC(8, 2) NOT NULL
);

DROP TABLE car, person;