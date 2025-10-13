package util

import Audio
import AudioAttachment
import Comment
import CommentThread
import Photo
import PhotoAttachment
import PhotoSize
import Post
import Video
import VideoAttachment


object TestData {

    fun createSimplePost(
        text: String = "Test post",
        ownerId: Int = 1,
        fromId: Int = 1
    ) = Post(
        text = text,
        ownerId = ownerId,
        fromId = fromId
    )

    fun createPostWithNullableFields(
        text: String = "Test post",
        signerId: Int? = null,
        createdBy: Int? = null
    ) = Post(
        text = text,
        ownerId = 1,
        fromId = 1,
        signerId = signerId,
        createdBy = createdBy
    )

    fun createSimpleComment(
        postId: Int = 1,
        text: String = "Test comment",
        fromId: Int = 2
    ) = Comment(
        postId = postId,
        fromId = fromId,
        text = text
    )

    fun createCommentWithThread(
        postId: Int = 1,
        text: String = "Comment with thread"
    ) = Comment(
        postId = postId,
        fromId = 1,
        text = text,
        thread = CommentThread(
            count = 3,
            items = listOf(
                createSimpleComment(postId, "Reply 1"),
                createSimpleComment(postId, "Reply 2")
            )
        )
    )

    fun createPhoto(): Photo {
        return Photo(
            id = 1,
            ownerId = 1,
            sizes = listOf(
                PhotoSize(
                    url = "https://example.com/photo_130.jpg",
                    width = 130,
                    height = 87,
                    type = "m"
                ),
                PhotoSize(
                    url = "https://example.com/photo_604.jpg",
                    width = 604,
                    height = 403,
                    type = "x"
                ),
                PhotoSize(
                    url = "https://example.com/photo_807.jpg",
                    width = 807,
                    height = 538,
                    type = "y"
                )
            )
        )
    }

    fun createPhotoAttachment(): PhotoAttachment {
        return PhotoAttachment(createPhoto())
    }

    fun createVideo(): Video {
        return Video(
            id = 1,
            ownerId = 1,
            title = "Test Video",
            duration = 120
        )
    }

    fun createVideoAttachment(): VideoAttachment {
        return VideoAttachment(createVideo())
    }

    fun createAudio(): Audio {
        return Audio(
            id = 1,
            ownerId = 1,
            artist = "Test Artist",
            title = "Test Song",
            duration = 180
        )
    }

    fun createAudioAttachment(): AudioAttachment {
        return AudioAttachment(createAudio())
    }

    fun createPostWithAttachments(vararg attachments: PhotoAttachment): Post {
        return Post(
            text = "Post with attachments",
            ownerId = 1,
            fromId = 1,
            attachments = attachments.toList()
        )
    }
}