package net.t53k

object Day06 {

    private fun findFirstMarker(current: List<Char>, position: Int = 1): Int {
        if(current.count() < 5) {
            return 0
        }
        if(allDifferent(current.slice(0..3))) {
            return position + 3
        }
        return findFirstMarker(current.slice(position until current.count()), position + 1)
    }

    private fun allDifferent(slice: List<Char>): Boolean {
        return slice.groupBy { it }.count() == slice.count()
    }

    fun firstMarkerPosition(input: String): Int {
        return findFirstMarker(input.toList())
    }
}