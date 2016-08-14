// IOnNewBookArrivedListener.aidl
package com.maqiang.aidl;

import com.maqiang.aidl.Book;


interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
