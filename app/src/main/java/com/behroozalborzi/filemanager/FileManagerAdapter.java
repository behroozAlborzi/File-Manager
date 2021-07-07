package com.behroozalborzi.filemanager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Behrooz on 7/3/2021.
 * https://behroozalborzi.ir
 * Android Developer
 * Thank you ... :)
 */
public class FileManagerAdapter extends RecyclerView.Adapter<FileManagerAdapter.FileManagerViewHolder> {

    private FileItemEventListener fileItemEventListener;
    private List<File> files;
    private List<File> filteredFiles;
    private ViewType viewType = ViewType.ROW;
    public FileManagerAdapter(List<File> files,FileItemEventListener fileItemEventListener){
//        Collections.sort(files, new Comparator<File>() {
//            @Override
//            public int compare(File o1, File o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
        this.files = new ArrayList<>(files);
        this.filteredFiles = this.files;
        this.fileItemEventListener = fileItemEventListener;

    }

    @NonNull
    @Override
    public FileManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileManagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                viewType==ViewType.ROW.getValue()?R.layout.item_files:R.layout.item_files_grid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileManagerViewHolder holder, int position) {
        holder.viewBinding(filteredFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    public void addFolder(File file){
        files.add(0,file);
        notifyItemInserted(0);
    }
    public void deleteFile(File file){
        int index = files.indexOf(file);
        if (index>-1){
            files.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void search(String query){
        if (query.length()>0){

            List<File> result = new ArrayList<>();
            for (File file :
                    this.files) {
                if (file.getName().toLowerCase().contains(query.toLowerCase()))
                    result.add(file);


                this.filteredFiles = result;
                notifyDataSetChanged();
            }
        }else {
            this.filteredFiles = this.files;
            notifyDataSetChanged();

        }
    }

    @Override
    public int getItemViewType(int position) {

        return viewType.getValue();
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    public class FileManagerViewHolder extends RecyclerView.ViewHolder{

        private ImageView fileIconIv;
        private TextView fileNameTv;
        private View moreId;
        public FileManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            fileIconIv = itemView.findViewById(R.id.iv_item_file);
            fileNameTv = itemView.findViewById(R.id.tv_item_fileName);
            moreId = itemView.findViewById(R.id.iv_item_more);
                }
        public void viewBinding(File file){
            if (file.isDirectory()){
                fileIconIv.setImageResource(R.drawable.ic_folder_black_32dp);
            }else {
                fileIconIv.setImageResource(R.drawable.ic_file_black_32dp);
            }

            fileNameTv.setText(file.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileItemEventListener.onFileItemClicked(file);
                }
            });

            moreId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                    popupMenu.getMenuInflater().inflate(R.menu.item_file,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_item_delete:
                                  fileItemEventListener.onItemDelete(file);
                                  break;
                                case R.id.menu_item_copy:
                                    fileItemEventListener.onItemCopy(file);
                                    break;
                                case R.id.menu_item_move:
                                    fileItemEventListener.onItemMove(file);
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });
        }
    }
}
