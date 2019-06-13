package seekbar.ggh.com.soundsrecord.audio_list;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import seekbar.ggh.com.soundsrecord.R;

public class ListAdapter extends BaseQuickAdapter<DataEntity, BaseViewHolder> {

    public ListAdapter(@Nullable List<DataEntity> data) {
        super(R.layout.item_list,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, DataEntity item) {
        helper.setText (R.id.file_name_text,item.getName ());
//        helper.setText (R.id.file_length_text, (int) item.getLength());
//        helper.setText (R.id.file_date_added_text, (int) item.getTime ());
//        ImageView imageView = helper.itemView.findViewById (R.id.iv_view);
//        Glide.with (mContext).load (item.getView ()).into (imageView);

    }
}
