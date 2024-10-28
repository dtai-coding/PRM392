package com.example.phoneapp.Adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneapp.Model.User;
import com.example.phoneapp.databinding.RowUserManageBinding;

import java.util.ArrayList;
import java.util.List;

public class UserManagementAdapter extends RecyclerView.Adapter<UserManagementAdapter.UserViewHolder> {

    private List<Pair<User, String>> usersWithKeys = new ArrayList<>();
    private OnItemClickListener listener;

    public void setUsers(List<Pair<User, String>> usersWithKeys) {
        this.usersWithKeys = usersWithKeys;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(RowUserManageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Pair<User, String> userWithKey = usersWithKeys.get(position);
        holder.bind(userWithKey.first, userWithKey.second); // Bind both user and userKey
    }

    @Override
    public int getItemCount() {
        return usersWithKeys.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private RowUserManageBinding binding;

        public UserViewHolder(@NonNull RowUserManageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user, String userKey) {
            binding.tvUserName.setText(user.getName());
            binding.tvPhone.setText(user.getPhone());
            binding.tvEmail.setText(user.getEmail());
            binding.tvRole.setText(user.getRole());

            binding.btnEdit.setOnClickListener(v -> listener.onEditClick(user, userKey));
            binding.btnDelete.setOnClickListener(v -> listener.onDeleteClick(userKey));
        }
    }

    public interface OnItemClickListener {
        void onEditClick(User user, String userKey);
        void onDeleteClick(String userKey);
    }
}
