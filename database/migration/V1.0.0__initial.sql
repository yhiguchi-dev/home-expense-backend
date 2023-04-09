CREATE TABLE expense.expense
(
    id           CHAR(36)                               NOT NULL,
    description  VARCHAR(512)                           NOT NULL,
    price        INTEGER                                NOT NULL,
    payment_date DATE                                   NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    CONSTRAINT pk_expense PRIMARY KEY (id)
);

CREATE TABLE expense.attribute
(
    id         CHAR(36)                               NOT NULL,
    category   VARCHAR(256)                           NOT NULL,
    name       VARCHAR(512)                           NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    CONSTRAINT pk_attribute PRIMARY KEY (id)
);

CREATE TABLE expense.fixed_expense
(
    expense_id   CHAR(36)                               NOT NULL,
    attribute_id CHAR(36)                               NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    CONSTRAINT pk_fixed_expense PRIMARY KEY (expense_id),
    FOREIGN KEY (expense_id) REFERENCES expense.expense (id) ON DELETE CASCADE
);

CREATE TABLE expense.variable_expense
(
    expense_id   CHAR(36)                               NOT NULL,
    attribute_id CHAR(36)                               NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    CONSTRAINT pk_variable_expense PRIMARY KEY (expense_id),
    FOREIGN KEY (expense_id) REFERENCES expense.expense (id) ON DELETE CASCADE
);
