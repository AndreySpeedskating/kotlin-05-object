data class Post(
    val id: Int = 0,
    val ownerId: Int = 0,
    val fromId: Int = 0,
    val date: Long = System.currentTimeMillis() / 1000,
    val text: String = "",
    val friendsOnly: Boolean = false,
    val postType: String = "post",
    val canPin: Boolean = false,
    val canDelete: Boolean = false,
    val canEdit: Boolean = false,
    val isPinned: Boolean = false,
    val markedAsAds: Boolean = false,
    val isFavorite: Boolean = false,

    // Nullable поля - только те, которые действительно могут отсутствовать
    val createdBy: Int? = null,
    val replyOwnerId: Int? = null,
    val replyPostId: Int? = null,
    val signerId: Int? = null,
    val postponedId: Int? = null,

    // Опциональные объекты
    val comments: Comments = Comments(),
    val likes: Likes = Likes(),
    val reposts: Reposts = Reposts(),
    val views: Views = Views(),
    val copyright: Copyright? = null,

    // Вложения
    val attachments: List<Attachment> = emptyList()
)

data class Copyright(
    val id: Int,
    val link: String,
    val name: String,
    val type: String
)

// Остальные data классы остаются простыми
data class Comments(
    val count: Int = 0,
    val canPost: Boolean = true
)

data class Likes(
    val count: Int = 0,
    val userLikes: Boolean = false,
    val canLike: Boolean = true
)

data class Reposts(
    val count: Int = 0,
    val userReposted: Boolean = false
)

data class Views(
    val count: Int = 0
)