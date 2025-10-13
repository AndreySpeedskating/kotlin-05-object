import exception.CommentNotFoundException
import exception.InvalidReasonException
import exception.PostNotFoundException

class WallService {
    private val posts = mutableListOf<Post>()
    private val comments = mutableListOf<Comment>()
    private val reports = mutableListOf<Report>()
    private var nextPostId = 1
    private var nextCommentId = 1
    private var nextReportId = 1

    // region Posts

    /**
     * Добавляет новый пост
     * @param post пост без ID
     * @return пост с присвоенным ID
     */
    fun add(post: Post): Post {
        require(post.id == 0) { "New post must have id = 0" }

        val newPost = post.copy(id = nextPostId++)
        posts.add(newPost)
        return newPost
    }

    /**
     * Обновляет существующий пост
     * @param post пост с существующим ID
     * @return true если пост был обновлен, false если не найден
     */
    fun update(post: Post): Boolean {
        val index = posts.indexOfFirst { it.id == post.id }
        return if (index != -1) {
            posts[index] = post
            true
        } else {
            false
        }
    }

    /**
     * Находит пост по ID
     * @param id идентификатор поста
     * @return пост или null если не найден
     */
    fun findById(id: Int): Post? = posts.find { it.id == id }

    /**
     * Находит пост по ID (синоним для findById для обратной совместимости)
     * @param id идентификатор поста
     * @return пост или null если не найден
     */
    fun findPostById(id: Int): Post? = findById(id)

    /**
     * Проверяет существование поста
     * @param id идентификатор поста
     * @return true если пост существует
     */
    fun postExists(id: Int): Boolean = posts.any { it.id == id }

    /**
     * Возвращает все посты
     */
    fun getAllPosts(): List<Post> = posts.toList()

    // endregion

    // region Comments

    /**
     * Создает комментарий к посту
     * @param postId идентификатор поста
     * @param comment комментарий без ID
     * @return комментарий с присвоенным ID
     * @throws PostNotFoundException если пост не найден
     */
    fun createComment(postId: Int, comment: Comment): Comment {
        require(comment.id == 0) { "New comment must have id = 0" }

        if (!postExists(postId)) {
            throw PostNotFoundException("Post with id $postId not found")
        }

        val newComment = comment.copy(
            id = nextCommentId++,
            postId = postId
        )
        comments.add(newComment)
        return newComment
    }

    /**
     * Находит комментарий по ID
     * @param id идентификатор комментария
     * @return комментарий или null если не найден
     */
    fun findCommentById(id: Int): Comment? = comments.find { it.id == id }

    /**
     * Получает все комментарии для поста
     * @param postId идентификатор поста
     * @return список комментариев
     */
    fun getCommentsForPost(postId: Int): List<Comment> =
        comments.filter { it.postId == postId }

    /**
     * Проверяет существование комментария
     * @param id идентификатор комментария
     * @return true если комментарий существует
     */
    fun commentExists(id: Int): Boolean = comments.any { it.id == id }

    // endregion

    // region Reports

    /**
     * Создает жалобу на комментарий
     * @param commentId идентификатор комментария
     * @param reason причина жалобы
     * @return созданная жалоба
     * @throws CommentNotFoundException если комментарий не найден
     * @throws InvalidReasonException если причина неверна
     */
    fun reportComment(commentId: Int, reason: Reason): Report {
        if (!commentExists(commentId)) {
            throw CommentNotFoundException("Comment with id $commentId not found")
        }

        val report = Report(
            id = nextReportId++,
            commentId = commentId,
            reason = reason
        )
        reports.add(report)
        return report
    }

    /**
     * Получает все жалобы для комментария
     * @param commentId идентификатор комментария
     * @return список жалоб
     */
    fun getReportsForComment(commentId: Int): List<Report> =
        reports.filter { it.commentId == commentId }

    /**
     * Получает все жалобы по причине
     * @param reason причина жалобы
     * @return список жалоб
     */
    fun getReportsByReason(reason: Reason): List<Report> =
        reports.filter { it.reason == reason }

    // endregion

    /**
     * Очищает сервис (для тестирования)
     */
    fun clear() {
        posts.clear()
        comments.clear()
        reports.clear()
        nextPostId = 1
        nextCommentId = 1
        nextReportId = 1
    }

    /**
     * Возвращает количество постов
     */
    fun postsCount(): Int = posts.size

    /**
     * Возвращает количество комментариев
     */
    fun commentsCount(): Int = comments.size

    /**
     * Возвращает количество жалоб
     */
    fun reportsCount(): Int = reports.size
}