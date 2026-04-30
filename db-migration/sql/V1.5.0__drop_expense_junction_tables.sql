-- 経費属性のカテゴリは attribute から JOIN で取得する設計に移行したため junction テーブルを廃止
DROP TABLE expense.fixed_expense;
DROP TABLE expense.variable_expense;
