# rbac

a java rbac admin project, use easyui

## easyui

extract easyui in `src/main/resources/static/`

## sql

truncate table with foreign key

```sql
SET
FOREIGN_KEY_CHECKS = 0;
TRUNCATE table $table_name;
SET
FOREIGN_KEY_CHECKS = 1;
```