-- expense.expense に attribute_id 列を追加し、junction テーブルからバックフィル
ALTER TABLE expense.expense ADD COLUMN attribute_id CHAR(36);

UPDATE expense.expense AS e
SET attribute_id = COALESCE(
  (SELECT attribute_id FROM expense.fixed_expense    WHERE expense_id = e.id),
  (SELECT attribute_id FROM expense.variable_expense WHERE expense_id = e.id)
);

ALTER TABLE expense.expense ALTER COLUMN attribute_id SET NOT NULL;

ALTER TABLE expense.expense
  ADD CONSTRAINT fk_expense_attribute
  FOREIGN KEY (attribute_id) REFERENCES expense.attribute(id);

CREATE INDEX idx_expense_attribute_id ON expense.expense(attribute_id);
