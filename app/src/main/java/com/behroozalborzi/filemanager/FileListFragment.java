package com.behroozalborzi.filemanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Behrooz on 7/3/2021.
 * https://behroozalborzi.ir
 * Android Developer
 * Thank you ... :)
 */
public class FileListFragment extends Fragment implements FileItemEventListener{

    private String path;
    private RecyclerView rv_frameContainer;
    private FileManagerAdapter fileManagerAdapter;
    private GridLayoutManager gridLayoutManager;
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString(MainActivity.PATH_FILE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_files,container,false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvPath = view.findViewById(R.id.tv_frameContainer_path);

        rv_frameContainer = view.findViewById(R.id.rv_frameContainer_list);
        File currentFolder = new File(path);
        if (StorageHelper.isExternalStorageReadable()){
            File[] files = currentFolder.listFiles();
            if (files != null && files.length>1){
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
            }

            if (files != null) {

                fileManagerAdapter = new FileManagerAdapter(Arrays.asList(files),this);
            }
        }
        gridLayoutManager = new GridLayoutManager(getContext(),1,RecyclerView.VERTICAL,false);
     //   rv_frameContainer.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        rv_frameContainer.setLayoutManager(gridLayoutManager);
        rv_frameContainer.setAdapter(fileManagerAdapter);

        tvPath.setText(currentFolder.getName().equalsIgnoreCase("files")?"External Storage":
                "External Storage/"+currentFolder.getName());

        view.findViewById(R.id.iv_frameContainer_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onFileItemClicked(File file) {
        if (file.isDirectory()){
            ((MainActivity)getActivity()).listFile(file.getPath());

        }
    }

    @Override
    public void onItemDelete(File file) {
        if (StorageHelper.isExternalStorageWritable()){
            if (file.delete()){
                fileManagerAdapter.deleteFile(file);
            }
        }

    }

    @Override
    public void onItemCopy(File file) {
        if (StorageHelper.isExternalStorageWritable()){
            try {
                copyFile(file,getDestinationFileDir(file.getName()));
                Toast.makeText(getContext(), "This File is copied", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onItemMove(File file) {
        if (StorageHelper.isExternalStorageWritable()){
            try {
            copyFile(file,getDestinationFileDir(file.getName()));
            onItemDelete(file);
            Toast.makeText(getContext(), "This File is Moved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void createNewFolder(String folderName){
        if (StorageHelper.isExternalStorageWritable()){
            File newFolder = new File(path+File.separator+folderName);
            if (!newFolder.exists()){
                if (newFolder.mkdir()){
                    fileManagerAdapter.addFolder(newFolder);
                    rv_frameContainer.smoothScrollToPosition(0);
                }
            }
        }

   }

   public void copyFile(File source , File destination) throws IOException {
       FileInputStream fileInputStream = new FileInputStream(source);
       FileOutputStream fileOutputStream = new FileOutputStream(destination);

       byte[] buffer = new byte[1024];
       int length ;
       while((length = fileInputStream.read(buffer))>0){

           fileOutputStream.write(buffer,0,length);
       }

       fileInputStream.close();
       fileOutputStream.close();

   }

   public File getDestinationFileDir(String fileName){
        return new File(getContext().getExternalFilesDir(null).getPath() + File.separator + "Destination" + File.separator + fileName);
   }

   public void search(String query){
        if (fileManagerAdapter != null)
            fileManagerAdapter.search(query);
   }

   public void setViewType(ViewType viewType){
        if (fileManagerAdapter != null){
            fileManagerAdapter.setViewType(viewType);
            if (viewType==ViewType.GRID){
                gridLayoutManager.setSpanCount(2);
            }else{
                gridLayoutManager.setSpanCount(1);
            }
        }
   }

//   public void moveFile(File source, File destination) throws IOException{
//        FileInputStream fileInputStream = new FileInputStream(source);
//        FileOutputStream fileOutputStream = new FileOutputStream(destination);
//
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = fileInputStream.read(buffer))>0){
//            fileOutputStream.write(buffer,0,length);
//        }
//
//        fileManagerAdapter.deleteFile(source);
//
//        fileInputStream.close();
//        fileOutputStream.close();
//   }

}
