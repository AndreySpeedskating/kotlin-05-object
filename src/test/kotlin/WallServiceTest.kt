import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WallServiceTest {

    @BeforeEach
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun addShouldSetNonZeroId() {
        val post = Post(
            ownerId = 123,
            fromId = 123,
            text = "Test post content",
            date = System.currentTimeMillis() / 1000,
            postType = "post"
        )

        val result = WallService.add(post)

        assertTrue(result.id > 0, "Post ID should be greater than 0 after adding")
        assertEquals("Test post content", result.text)
        assertEquals(123, result.ownerId)
    }

    @Test
    fun addShouldIncrementIdForEachPost() {
        val post1 = Post(text = "First post")
        val post2 = Post(text = "Second post")
        val post3 = Post(text = "Third post")

        val result1 = WallService.add(post1)
        val result2 = WallService.add(post2)
        val result3 = WallService.add(post3)

        assertEquals(1, result1.id)
        assertEquals(2, result2.id)
        assertEquals(3, result3.id)
        assertNotEquals(result1.id, result2.id)
        assertNotEquals(result2.id, result3.id)
    }

    @Test
    fun updateExistingPostShouldReturnTrueAndUpdateFields() {
        val originalPost = WallService.add(
            Post(
                text = "Original post text",
                date = 1000000L,
                likes = Likes(count = 5)
            )
        )

        val updatedPost = originalPost.copy(
            text = "Updated post text",
            date = 2000000L,
            likes = Likes(count = 10)
        )

        val result = WallService.update(updatedPost)

        assertTrue(result, "Update should return true for existing post")

        val postsAfterUpdate = WallService.getPosts()
        assertEquals(1, postsAfterUpdate.size)
        assertEquals("Updated post text", postsAfterUpdate[0].text)
        assertEquals(2000000L, postsAfterUpdate[0].date)
        assertEquals(10, postsAfterUpdate[0].likes.count)
    }

    @Test
    fun updateNonExistingPostShouldReturnFalse() {
        WallService.add(Post(text = "First post"))
        WallService.add(Post(text = "Second post"))

        val nonExistingPost = Post(
            id = 999,
            text = "Non-existing post",
            date = System.currentTimeMillis() / 1000
        )

        val result = WallService.update(nonExistingPost)

        assertFalse(result, "Update should return false for non-existing post")

        val posts = WallService.getPosts()
        assertEquals(2, posts.size)

        assertTrue(posts.all { it.id != 999 })
    }

    @Test
    fun updateShouldNotChangePostIdSequence() {
        val post1 = WallService.add(Post(text = "Post 1"))
        val post2 = WallService.add(Post(text = "Post 2"))
        val post3 = WallService.add(Post(text = "Post 3"))

        val updatedPost2 = post2.copy(text = "Updated Post 2")

        val updateResult = WallService.update(updatedPost2)
        val newPost = WallService.add(Post(text = "Post 4"))

        assertTrue(updateResult)
        assertEquals(4, newPost.id, "New post should get next ID after update")

        val posts = WallService.getPosts()
        assertEquals(4, posts.size)
        assertEquals(listOf(1, 2, 3, 4), posts.map { it.id })
    }

    @Test
    fun clearShouldResetPostsAndIdCounter() {
        WallService.add(Post(text = "First post"))
        WallService.add(Post(text = "Second post"))

        WallService.clear()

        val postsAfterClear = WallService.getPosts()
        assertTrue(postsAfterClear.isEmpty(), "Posts array should be empty after clear")

        val newPost = WallService.add(Post(text = "New post after clear"))
        assertEquals(1, newPost.id, "First post after clear should have ID = 1")
    }

    @Test
    fun getPostsShouldReturnCopyOfPostsArray() {

        val post1 = WallService.add(Post(text = "Post 1"))
        val post2 = WallService.add(Post(text = "Post 2"))

        val posts = WallService.getPosts()

        assertEquals(2, posts.size)
        assertEquals("Post 1", posts[0].text)
        assertEquals("Post 2", posts[1].text)
        assertEquals(post1.id, posts[0].id)
        assertEquals(post2.id, posts[1].id)
    }

    @Test
    fun addPostWithCommentsAndLikes() {
        // Arrange
        val post = Post(
            text = "Post with engagement",
            comments = Comments(count = 5, canPost = true),
            likes = Likes(count = 10, userLikes = true),
            reposts = Reposts(count = 2),
            views = Views(count = 100)
        )

        val result = WallService.add(post)

        assertEquals(5, result.comments.count)
        assertTrue(result.comments.canPost)
        assertEquals(10, result.likes.count)
        assertTrue(result.likes.userLikes)
        assertEquals(2, result.reposts.count)
        assertEquals(100, result.views.count)
    }

    @Test
    fun updatePostWithComplexObjects() {
        val originalPost = WallService.add(
            Post(
                text = "Original",
                comments = Comments(count = 1),
                likes = Likes(count = 1)
            )
        )

        val updatedPost = originalPost.copy(
            text = "Updated",
            comments = Comments(count = 10, canPost = false),
            likes = Likes(count = 20, userLikes = true),
            reposts = Reposts(count = 5, userReposted = true),
            views = Views(count = 500)
        )

        val result = WallService.update(updatedPost)

        assertTrue(result)
        val posts = WallService.getPosts()
        val actualPost = posts[0]

        assertEquals("Updated", actualPost.text)
        assertEquals(10, actualPost.comments.count)
        assertFalse(actualPost.comments.canPost)
        assertEquals(20, actualPost.likes.count)
        assertTrue(actualPost.likes.userLikes)
        assertEquals(5, actualPost.reposts.count)
        assertTrue(actualPost.reposts.userReposted)
        assertEquals(500, actualPost.views.count)
    }
}