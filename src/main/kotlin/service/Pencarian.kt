package service

import model.Item

interface Pencarian {
    fun cariItem(keyword: String): List<Item>
}
