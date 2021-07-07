package com.behroozalborzi.filemanager;

import java.io.File;

/**
 * Created by Behrooz on 7/4/2021.
 * https://behroozalborzi.ir
 * Android Developer
 * Thank you ... :)
 */
public interface FileItemEventListener {

    void onFileItemClicked(File file);
    void onItemDelete(File file);
    void onItemCopy(File file);
    void onItemMove(File file);
}
