sealed interface Attachment {
    val type: String
}

data class Photo(
    val id: Int,
    val ownerId: Int,
    val sizes: List<PhotoSize>
)

data class PhotoSize(
    val url: String,
    val width: Int,
    val height: Int,
    val type: String
)

data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val duration: Int
)

data class Audio(
    val id: Int,
    val ownerId: Int,
    val artist: String,
    val title: String,
    val duration: Int
)

data class Document(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val size: Int,
    val ext: String
)

data class Link(
    val url: String,
    val title: String,
    val description: String? = null
)

// Реализации sealed interface
data class PhotoAttachment(val photo: Photo) : Attachment {
    override val type: String = "photo"
}

data class VideoAttachment(val video: Video) : Attachment {
    override val type: String = "video"
}

data class AudioAttachment(val audio: Audio) : Attachment {
    override val type: String = "audio"
}

data class DocumentAttachment(val document: Document) : Attachment {
    override val type: String = "document"
}

data class LinkAttachment(val link: Link) : Attachment {
    override val type: String = "link"
}