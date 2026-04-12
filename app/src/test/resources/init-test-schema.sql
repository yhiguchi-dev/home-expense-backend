CREATE SCHEMA IF NOT EXISTS expense;

CREATE TABLE IF NOT EXISTS expense.expense
(
    id           CHAR(36)                               NOT NULL,
    description  VARCHAR(512)                           NOT NULL,
    price        INTEGER                                NOT NULL,
    payment_date DATE                                   NOT NULL,
    version      BIGINT                                 NOT NULL DEFAULT 1,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_expense PRIMARY KEY (id),
    CONSTRAINT ck_expense_price_positive CHECK (price > 0)
);

CREATE TABLE IF NOT EXISTS expense.attribute
(
    id         CHAR(36)                               NOT NULL,
    category   VARCHAR(256)                           NOT NULL,
    name       VARCHAR(512)                           NOT NULL,
    version    BIGINT                                 NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_attribute PRIMARY KEY (id),
    CONSTRAINT uq_attribute_name_category UNIQUE (name, category)
);

CREATE TABLE IF NOT EXISTS expense.fixed_expense
(
    expense_id   CHAR(36)                               NOT NULL,
    attribute_id CHAR(36)                               NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_fixed_expense PRIMARY KEY (expense_id),
    FOREIGN KEY (expense_id) REFERENCES expense.expense (id) ON DELETE CASCADE,
    FOREIGN KEY (attribute_id) REFERENCES expense.attribute (id)
);

CREATE TABLE IF NOT EXISTS expense.variable_expense
(
    expense_id   CHAR(36)                               NOT NULL,
    attribute_id CHAR(36)                               NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_variable_expense PRIMARY KEY (expense_id),
    FOREIGN KEY (expense_id) REFERENCES expense.expense (id) ON DELETE CASCADE,
    FOREIGN KEY (attribute_id) REFERENCES expense.attribute (id)
);

CREATE INDEX IF NOT EXISTS idx_attribute_category on expense.attribute(category);
CREATE INDEX IF NOT EXISTS idx_attribute_name on expense.attribute(name);

CREATE TABLE IF NOT EXISTS expense.income_attribute
(
    id         CHAR(36)                               NOT NULL,
    name       VARCHAR(512)                           NOT NULL,
    version    BIGINT                                 NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_income_attribute PRIMARY KEY (id),
    CONSTRAINT uq_income_attribute_name UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS idx_income_attribute_name on expense.income_attribute(name);

CREATE TABLE IF NOT EXISTS expense.income
(
    id           CHAR(36)                               NOT NULL,
    attribute_id CHAR(36)                               NOT NULL,
    description  VARCHAR(512)                           NOT NULL,
    amount       INTEGER                                NOT NULL,
    receive_date DATE                                   NOT NULL,
    version      BIGINT                                 NOT NULL DEFAULT 1,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_income PRIMARY KEY (id),
    CONSTRAINT ck_income_amount_positive CHECK (amount > 0),
    FOREIGN KEY (attribute_id) REFERENCES expense.income_attribute (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_expense_payment_date ON expense.expense(payment_date);
CREATE INDEX IF NOT EXISTS idx_income_receive_date ON expense.income(receive_date);
CREATE INDEX IF NOT EXISTS idx_income_attribute_id ON expense.income(attribute_id);
