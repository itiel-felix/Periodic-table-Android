package com.itielfelix.practica4_recyclerview

import android.os.Parcel
import android.os.Parcelable

data class Element(val atomicNum:Int, val symbol: String, val name:String, val atomicMass:String, val oxidationStates:String, val state:String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(atomicNum)
        parcel.writeString(symbol)
        parcel.writeString(name)
        parcel.writeString(atomicMass)
        parcel.writeString(oxidationStates)
        parcel.writeString(state)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(parcel: Parcel): Element {
            return Element(parcel)
        }

        override fun newArray(size: Int): Array<Element?> {
            return arrayOfNulls(size)
        }
    }
}

