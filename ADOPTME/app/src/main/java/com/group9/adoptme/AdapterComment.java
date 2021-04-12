package com.group9.adoptme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.CommentViewHolder> {
    private Context mCtx;
    private List<PetComments> petCommentsList;

    public AdapterComment(Context mCtx, List<PetComments> petCommentsList) {
        this.mCtx = mCtx;
        this.petCommentsList = petCommentsList;
    }

    public AdapterComment(ADMRatings mCtx, List<RatingReviews> ratingReviewsList) {
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_comment, parent, false);
        return new AdapterComment.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {
        PetComments petComments = petCommentsList.get(position);
        holder.fullname.setText( petComments.getFullname());
        holder.comments.setText( petComments.getComments());
        holder.commentDate.setText(petComments.getCommentDate());
        Glide.with(mCtx)
                .load( petComments.getUserphoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.userphoto);
    }

    @Override
    public int getItemCount() {
        return petCommentsList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView comments, commentDate, fullname;
        ImageView userphoto;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userphoto = itemView.findViewById(R.id.com_picture);
            fullname = itemView.findViewById(R.id.com_textuser);
            comments = itemView.findViewById(R.id.view_comment);
            commentDate = itemView.findViewById(R.id.com_textdate);

        }
    }
}
