class WallService {
    private val posts = mutableListOf<Post>()
    private var nextId = 1

    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId++)
        posts.add(newPost)
        return newPost
    }

    fun update(post: Post): Boolean {
        val index = posts.indexOfFirst { it.id == post.id }
        return if (index != -1) {
            posts[index] = post
            true
        } else {
            false
        }
    }

    fun clear() {
        posts.clear()
        nextId = 1
    }

    fun findById(id: Int): Post? = posts.find { it.id == id }

    fun getPosts(): List<Post> = posts.toList()
}