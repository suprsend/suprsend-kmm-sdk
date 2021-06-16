package app.suprsend.android


class UserDao(suprSend: SuprSend) {

    private val db = suprSend.userQueries

    internal fun insert(id: String, name: String, company: Company) {
        db.insertItem(
            id = id,
            name = name,
            company = company
        )
    }

    internal fun select(): List<UserModel> = db.selectAll().executeAsList()
}