CREATE TABLE expense.income_attribute
(
    id         CHAR(36)                               NOT NULL,
    name       VARCHAR(512)                           NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    CONSTRAINT pk_income_attribute PRIMARY KEY (id)
);

CREATE TABLE expense.income
(
    id           CHAR(36)                               NOT NULL,
    attribute_id CHAR(36)                               NOT NULL,
    description  VARCHAR(512)                           NOT NULL,
    amount        INTEGER                                NOT NULL,
    receive_date DATE                                   NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    CONSTRAINT pk_expense PRIMARY KEY (id),
    FOREIGN KEY (attribute_id) REFERENCES expense.income_attribute (id) ON DELETE CASCADE
);
