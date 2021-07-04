package com.android.testproject1.model

import java.util.ArrayList

object DataProvider {

    val productList: MutableList<Product> = ArrayList()

    private fun addProduct(image: String) {

        val item = Product(image)
        productList.add(item)
    }


    val productExploreList:MutableList<ProductExplore> = ArrayList()
    private fun addProductExplore(image: String){

        val exploreItem = ProductExplore(image)
        productExploreList.add(exploreItem)


    }


    init {
//        addProduct("https://images.unsplash.com/photo-1493612276216-ee3925520721?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1900&q=80")
//        addProduct("https://images.unsplash.com/photo-1598128558393-70ff21433be0?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1922&q=80")
//        addProduct("https://images.unsplash.com/photo-1581090700227-1e37b190418e?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80")
//        addProduct("https://images.unsplash.com/photo-1581092918056-0c4c3acd3789?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=1050&q=80")
//        addProduct("https://images.unsplash.com/photo-1473968512647-3e447244af8f?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80")
//        addProduct("https://images.unsplash.com/photo-1494253109108-2e30c049369b?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=1050&q=80")

        addProduct("https://images.unsplash.com/photo-1610085927744-7217728267a6?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=329&q=80")
        addProduct("https://images.unsplash.com/photo-1604442993086-26abda47465e?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1051&q=80")
        addProduct("https://images.unsplash.com/photo-1565182396194-7e2a07c32c2a?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1055&q=80")
        addProduct("https://images.unsplash.com/photo-1587033649720-6850605eb2f1?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1018&q=80")
        addProduct("https://images.unsplash.com/photo-1600106485793-d463d45905bf?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=794&q=80")
        addProduct("https://images.unsplash.com/photo-1586881141091-1014c7c2cb79?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1048&q=80")
        addProduct("https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1934&q=80")
        addProduct("https://images.unsplash.com/photo-1599661046827-dacff0c0f09a?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=1950&q=80")
        addProduct("https://images.unsplash.com/photo-1561909381-3d716364ad47?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1944&q=80")





        addProductExplore("https://images.unsplash.com/photo-1587033649720-6850605eb2f1?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1018&q=80")
        addProductExplore("https://images.unsplash.com/photo-1600106485793-d463d45905bf?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=794&q=80")
        addProductExplore("https://images.unsplash.com/photo-1586881141091-1014c7c2cb79?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1048&q=80")
        addProductExplore("https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1934&q=80")
        addProductExplore("https://images.unsplash.com/photo-1599661046827-dacff0c0f09a?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=1950&q=80")
        addProductExplore("https://images.unsplash.com/photo-1565182396194-7e2a07c32c2a?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1055&q=80")
        addProductExplore("https://images.unsplash.com/photo-1561909381-3d716364ad47?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1944&q=80")
        addProductExplore("https://images.unsplash.com/photo-1610085927744-7217728267a6?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=329&q=80")
        addProductExplore("https://images.unsplash.com/photo-1604442993086-26abda47465e?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1051&q=80")

    }



}