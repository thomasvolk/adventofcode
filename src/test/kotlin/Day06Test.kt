package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day06Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day06-input.txt")!!

    @Test
    fun testDay06part1() {
        assertEquals(7, Day06.firstMarkerPosition("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
        assertEquals(5, Day06.firstMarkerPosition("bvwbjplbgvbhsrlpgdmjqwftvncz"))
        assertEquals(6, Day06.firstMarkerPosition("nppdvjthqldpwncqszvftbrmjlhg"))
        assertEquals(11, Day06.firstMarkerPosition("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))
        assertEquals(10, Day06.firstMarkerPosition("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"))
    }
}