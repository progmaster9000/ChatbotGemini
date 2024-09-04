package theweeb.dev.chatbotbiometricauth.model

class User(
    val id: Int,
    val username: String,
    val usertype: UserType
)

enum class UserType{
    ADMIN,
    REGULAR,
    GUEST
}