package com.android.testproject1.model

import android.os.Parcel
import android.os.Parcelable

open class Images : Parcelable {
    var name: String?
    var og_path: String?
    var path: String?
    var id: Long

    constructor(name: String?, og_path: String?, path: String?, id: Long) {
        this.name = name
        this.og_path = og_path
        this.path = path
        this.id = id
    }

    protected constructor(`in`: Parcel) {
        name = `in`.readString()
        og_path = `in`.readString()
        path = `in`.readString()
        id = `in`.readLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(og_path)
        dest.writeString(path)
        dest.writeLong(id)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Images?> = object : Parcelable.Creator<Images?> {
            override fun createFromParcel(`in`: Parcel): Images? {
                return Images(`in`)
            }

            override fun newArray(size: Int): Array<Images?> {
                return arrayOfNulls(size)
            }
        }
    }
}
