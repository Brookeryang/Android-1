// IBookManager.aidl
package com.maqiang.aidl;

import com.maqiang.aidl.Book;
import com.maqiang.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
