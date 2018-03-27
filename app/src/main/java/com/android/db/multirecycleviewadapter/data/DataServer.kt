package com.android.db.multirecycleviewadapter.data


import com.android.db.multirecycleviewadapter.ientity.MultipleItem
import com.android.db.multirecycleviewadapter.ientity.MySection
import com.android.db.multirecycleviewadapter.ientity.Status
import com.android.db.multirecycleviewadapter.ientity.Video
import java.util.*

object DataServer {

    private val HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK = "https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"
    private val CYM_CHAD = "CymChad"

    val sampleData: List<MySection>
        get() {
            val list = ArrayList<MySection>()
            list.add(MySection(true, "Section 1", true))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(true, "Section 2", false))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(true, "Section 3", false))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(true, "Section 4", false))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(true, "Section 5", false))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            list.add(MySection(Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)))
            return list
        }

    val strData: List<String>
        get() {
            val list = ArrayList<String>()
            for (i in 0..19) {
                var str = HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK
                if (i % 2 == 0) {
                    str = CYM_CHAD
                }
                list.add(str)
            }
            return list
        }

    val multipleItemData: List<MultipleItem>
        get() {
            val list = ArrayList<MultipleItem>()
            for (i in 0..4) {
                list.add(MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE))
                list.add(MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, CYM_CHAD))
                list.add(MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE))
                list.add(MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN))
                list.add(MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN))
            }

            return list
        }

    fun getSampleData(lenth: Int): List<Status> {
        val list = ArrayList<Status>()
        for (i in 0 until lenth) {
            val status = Status()
            status.userName = "Chad$i"
            status.createdAt = "04/05/$i"
            status.userAvatar = "https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"
            status.text = "BaseRecyclerViewAdpaterHelper https://www.recyclerview.org"
            list.add(status)
        }
        return list
    }

    fun addData(list: MutableList<Status>, dataSize: Int): List<Status> {
        for (i in 0 until dataSize) {
            val status = Status()
            status.userName = "Chad$i"
            status.createdAt = "04/05/$i"
            status.userAvatar = "https://avatars1.githubusercontent.com/u/7698209?v=3&s=460"
            status.text = "Powerful and flexible RecyclerAdapter https://github.com/CymChad/BaseRecyclerViewAdapterHelper"
            list.add(status)
        }
        return list
    }

}
