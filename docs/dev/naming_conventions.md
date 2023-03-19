# Naming Conventions

## Use `_` for SQLite names

Makes it easier to write test queries in Android Studio without having to use backticks.

eg. `@ColumnInfo(name = "type_id") val typeId: String`

For api fields, we obviously have to use whatever they named it.

eg. `@Json(name = "type-id") val typeId: String`

