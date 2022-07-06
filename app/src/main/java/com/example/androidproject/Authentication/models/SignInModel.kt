
data class SignInModel(
    val code: String,
    val `data`: Response,
    val message: String
)

data class Response(
    val count: Int,
    val `data`: UserInfo
)

data class Token(
    val client_id: Int,
    val created_at: String,
    val expires_at: String,
    val id: String,
    val name: String,
    val revoked: Boolean,
    val scopes: List<Any>,
    val updated_at: String,
    val user_id: Int
)

data class UserInfo(
    val category: String,
    val country_code: String,
    val created_at: String,
    val email: Any,
    val email_verified_at: Any,
    val id: Int,
    val image: String,
    val isverified: String,
    val kitchen_id: Int,
    val lat: Int,
    val licence_image: Any,
    val licence_number: Any,
    val lng: Int,
    val mobile_number: Long,
    val mobilenumber: String,
    val name: String,
    val status: String,
    val subdomain: String,
    val token: String,
    val tokens: List<Token>,
    val updated_at: String,
    val vendor_id: Int
)