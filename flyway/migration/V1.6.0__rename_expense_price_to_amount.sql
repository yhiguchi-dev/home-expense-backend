-- 経費の price 列を amount にリネーム（収入の amount と用語を統一）
ALTER TABLE expense.expense RENAME COLUMN price TO amount;
ALTER TABLE expense.expense
  RENAME CONSTRAINT ck_expense_price_positive TO ck_expense_amount_positive;
