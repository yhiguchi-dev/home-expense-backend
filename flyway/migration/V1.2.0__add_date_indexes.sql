CREATE INDEX IF NOT EXISTS idx_expense_payment_date ON expense.expense(payment_date);
CREATE INDEX IF NOT EXISTS idx_income_receive_date ON expense.income(receive_date);
CREATE INDEX IF NOT EXISTS idx_income_attribute_id ON expense.income(attribute_id);
