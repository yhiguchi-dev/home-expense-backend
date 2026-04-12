-- version カラム追加
ALTER TABLE expense.expense ADD COLUMN version BIGINT NOT NULL DEFAULT 1;
ALTER TABLE expense.attribute ADD COLUMN version BIGINT NOT NULL DEFAULT 1;
ALTER TABLE expense.income ADD COLUMN version BIGINT NOT NULL DEFAULT 1;
ALTER TABLE expense.income_attribute ADD COLUMN version BIGINT NOT NULL DEFAULT 1;

-- CHECK 制約: 金額は正の値
ALTER TABLE expense.expense ADD CONSTRAINT ck_expense_price_positive CHECK (price > 0);
ALTER TABLE expense.income ADD CONSTRAINT ck_income_amount_positive CHECK (amount > 0);

-- FK 制約: junction テーブルの attribute_id
ALTER TABLE expense.fixed_expense ADD CONSTRAINT fk_fixed_expense_attribute
    FOREIGN KEY (attribute_id) REFERENCES expense.attribute(id);
ALTER TABLE expense.variable_expense ADD CONSTRAINT fk_variable_expense_attribute
    FOREIGN KEY (attribute_id) REFERENCES expense.attribute(id);

-- UNIQUE 制約: 属性名の重複防止 (#7a TOCTOU 対策)
ALTER TABLE expense.attribute ADD CONSTRAINT uq_attribute_name_category UNIQUE (name, category);
ALTER TABLE expense.income_attribute ADD CONSTRAINT uq_income_attribute_name UNIQUE (name);
