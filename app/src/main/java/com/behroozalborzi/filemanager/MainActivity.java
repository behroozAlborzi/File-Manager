package com.behroozalborzi.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AddFolderCallBack{

    public static final String PATH_FILE="path";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (StorageHelper.isExternalStorageReadable()){
            File file =getExternalFilesDir(null);
            listFile(file.getPath(),false);
        }


        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroup_fileManager);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if (checkedId==R.id.btn_list_horizontal && isChecked){
                    Fragment  fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fileManager_frameContainer);
                    if (fragment instanceof FileListFragment){
                        ((FileListFragment) fragment).setViewType(ViewType.ROW);
                    }
                }else if (checkedId==R.id.btn_list_grid && isChecked){
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fileManager_frameContainer);
                    if (fragment instanceof FileListFragment){
                        ((FileListFragment) fragment).setViewType(ViewType.GRID);
                    }
                }
            }
        });

        EditText editTextSearch = findViewById(R.id.et_fileManager_searchBox);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fileManager_frameContainer);
                if (fragment instanceof FileListFragment){
                    ((FileListFragment) fragment).search(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView iv_addNewFolder = findViewById(R.id.iv_toolbarAddNewFolder);
        iv_addNewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewFolderDialog addNewFolderDialog = new AddNewFolderDialog();
                addNewFolderDialog.show(getSupportFragmentManager(),null);
            }
        });

    }
    public void listFile(String path,boolean addToBackStack){
        FileListFragment fileListFragment = new FileListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH_FILE,path);
        fileListFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_fileManager_frameContainer,fileListFragment);
        if (addToBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
    public void listFile(String path){
        this.listFile(path,true);
    }

    @Override
    public void onAddFolderClicked(String folderName) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fileManager_frameContainer);
        if (fragment instanceof FileListFragment){
            ((FileListFragment) fragment).createNewFolder(folderName);
        }
    }
}