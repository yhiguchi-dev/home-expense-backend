CREATE TABLE expense.expense
(
    id           CHAR(36)                               NOT NULL,
    description  VARCHAR(512)                           NOT NULL,
    price        INTEGER                                NOT NULL,
    payment_date DATE                                   NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    CONSTRAINT pk_expense PRIMARY KEY (id)
);
