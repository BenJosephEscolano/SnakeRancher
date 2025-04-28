fun <T> MutableList<T>.extRemoveLast(): T {
    if (isEmpty()) throw NoSuchElementException("List is empty.")
    return removeAt(size - 1)
}