package com.itielfelix.practica4_recyclerview

import android.os.Parcel
import android.os.Parcelable


data class Filter(var order: String, var letter: String, var family: List<String>, var states:List<String>): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(order)
        parcel.writeString(letter)
        parcel.createStringArrayList()
        parcel.createStringArrayList()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Filter> {
        override fun createFromParcel(parcel: Parcel): Filter {
            return Filter(parcel)
        }

        override fun newArray(size: Int): Array<Filter?> {
            return arrayOfNulls(size)
        }
    }
}