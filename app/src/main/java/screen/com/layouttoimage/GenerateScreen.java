package screen.com.layouttoimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Arpit Singhal on 10/6/2017.
 */


/**
 * This class generated the Bitmap for a particular screen
 */
public class GenerateScreen {


    LayoutInflater mLayoutInflator;
    Context mContext;
    View mTopViewHolder;


    private static final String HEADER_FIRST = "FirstLine";
    private static final String HEADER_SECOND = "SecondLine";
    private static final String FOOTER_FIRST = "FirstLine";
    private static final String GRAND_TOTAL = "Grand Total";
    private static final String TAG = GenerateScreen.class.getSimpleName();
    private static final String RECEIPT_NAME = "MyScreen.jpg";

    /**
     * Class to contain Textviews for Description,Amount and price.
     * Used for Item Details part in Receipt
     */
    class ItemsDetailPortion {

        @BindView(R.id.description_view)
        TextView mDescription;
        @BindView(R.id.quantity_view)
        TextView mQuantity;
        @BindView(R.id.amount_view)
        TextView mAmount;

        ItemsDetailPortion(View iView) {
            ButterKnife.bind(this, iView);

        }
    }

    /**
     * Class to contain Textviews for Header and Footer part in Receipt.
     * Used for Header/Footer part in Receipt
     */
    class HeaderFooterPortion {

        @BindView(R.id.content)
        TextView mContent;

        HeaderFooterPortion(View iView) {
            ButterKnife.bind(this, iView);

        }
    }

    /**
     * This method is used to generate the image of the particular layout.
     *
     * @param context Context
     */
    public void drawScreen(Context context) {
        try {
            mContext = context;
            mLayoutInflator = LayoutInflater.from(mContext);
            //mTopViewHolder is the layout for the screen we want to get screenshot..
            mTopViewHolder = mLayoutInflator.inflate(R.layout.layout_screen_capture, null, false);
            Log.d(TAG, "Top View is initialised");

            createHeaderPortion();
            createItemDetailsPortion();
            createFooterPortion();
            //Convert the main layout after creating all the parts
            Bitmap screenToSave = viewToBitmap(mTopViewHolder);
            if (screenToSave != null) {
                //If all success,Then save to
                saveToPictures(screenToSave, RECEIPT_NAME);
            } else {
                Log.d(TAG, "Bitmap was null. Cannot save.");
                Toast.makeText(mContext, Constants.SAVE_ERROR_MSG, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d(TAG, "" + e);
        }
    }

    /**
     * This method created the Header Portion in the receipt
     */
    private void createHeaderPortion() {

        LinearLayout headerView = (LinearLayout) mTopViewHolder.findViewById(R.id.header_view);
        LinkedHashMap<String, String> headerDetailsMap = getHeaderDetails();
        // Check the map and init views
        if (headerDetailsMap != null && !headerDetailsMap.isEmpty()) {

            for (Map.Entry<String, String> entry : headerDetailsMap.entrySet()) {
                Log.d("GenerateScreen", "Key and Value : " + entry.getKey() + "  ---- " + entry.getValue());

                View view = mLayoutInflator.inflate(R.layout.view_header_footer, null);
                HeaderFooterPortion contentView = new HeaderFooterPortion(view);
                contentView.mContent.setText(entry.getValue());

                // Add the KeyValue view to the Transaction Detail Holder View
                headerView.addView(view);
            }
        }

    }

    /**
     * This method sets the Dummydetails wo be displayed in Header portion of receipt
     *
     * @return returns the map containing the Headers details.
     */
    private LinkedHashMap<String, String> getHeaderDetails() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43

        LinkedHashMap<String, String> headerDetails = new LinkedHashMap<>();
        headerDetails.put(HEADER_FIRST, "" + dateFormat.format(date));
        headerDetails.put(GenerateScreen.HEADER_SECOND, Constants.HEADER_MESSAGE_SECOND);
        return headerDetails;
    }


    /**
     * This method created the Item Detail Portion in the receipt
     */
    private void createItemDetailsPortion() {

        LinearLayout itemDetailsView = (LinearLayout) mTopViewHolder.findViewById(R.id.transaction_detail_holder);
        LinkedHashMap<Integer, Item> headerDetailsMap = geItemDetails();
        // Check the map and init views
        if (headerDetailsMap != null && !headerDetailsMap.isEmpty()) {

            for (Map.Entry<Integer, Item> entry : headerDetailsMap.entrySet()) {
                Log.d("GenerateScreen", "Key and Value : " + entry.getKey() + "  ---- " + entry.getValue());

                View view = mLayoutInflator.inflate(R.layout.view_receipt_item_details, null);
                ItemsDetailPortion contentView = new ItemsDetailPortion(view);
                Item item = entry.getValue();
                contentView.mDescription.setText(item.getDescription());
                contentView.mQuantity.setText(String.valueOf(item.getQuantity()));
                contentView.mAmount.setText(String.valueOf(item.getAmount()));
                // Add the KeyValue view to the Transaction Detail Holder View
                itemDetailsView.addView(view);
            }
        }
    }

    /**
     * This method sets the Dummy item details wo be displayed in Item Details portion of receipt
     *
     * @return returns the map containing the item purchased details.
     */
    private LinkedHashMap<Integer, Item> geItemDetails() {
        LinkedHashMap<Integer, Item> itemDetails = new LinkedHashMap<>();
        itemDetails.put(1, Item.getItem("Lenovo Laptop", 2, 3500));
        itemDetails.put(2, Item.getItem("Pen Drive", 4, 500));
        itemDetails.put(3, Item.getItem("Moto Mobile", 1, 10000));
        itemDetails.put(4, Item.getItem("USB", 3, 100));

        return itemDetails;
    }


    /**
     * This method created the Footer Portion in the receipt
     */
    private void createFooterPortion() {

        LinearLayout headerView = (LinearLayout) mTopViewHolder.findViewById(R.id.footer_view);
        LinkedHashMap<String, String> headerDetailsMap = getFooterDetails();
        // Check the map and init views
        if (headerDetailsMap != null && !headerDetailsMap.isEmpty()) {

            for (Map.Entry<String, String> entry : headerDetailsMap.entrySet()) {
                Log.d("GenerateScreen", "Key and Value : " + entry.getKey() + "  ---- " + entry.getValue());

                View view = mLayoutInflator.inflate(R.layout.view_header_footer, null);
                HeaderFooterPortion contentView = new HeaderFooterPortion(view);
                contentView.mContent.setText(entry.getValue());

                // Add the KeyValue view to the Transaction Detail Holder View
                headerView.addView(view);
            }
        }

    }

    /**
     * This method sets the Dummydetails wo be displayed in Footer portion of receipt
     *
     * @return returns the map containing the Footer details.
     */
    private LinkedHashMap<String, String> getFooterDetails() {

        LinkedHashMap<String, String> footerDetails = new LinkedHashMap<>();
        footerDetails.put(GRAND_TOTAL, Constants.FINAL_AMOUNT_TEXT + Item.grandTotal);
        footerDetails.put(GenerateScreen.FOOTER_FIRST, Constants.FOOTER_FIRST_MSG);
        Item.grandTotal = 0;
        return footerDetails;
    }

    /**
     * Creates bitmap from view's cache
     *
     * @param iView view of which bitmap has to be created
     * @return bitmap
     */

    private Bitmap viewToBitmap(View iView) {
        //Converts a view and all it's children into a bitmap
        Bitmap bitmapFromView = null;

        try {
            iView.setDrawingCacheEnabled(true);
            iView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            iView.layout(0, 0, iView.getMeasuredWidth(), iView.getMeasuredHeight());
            iView.buildDrawingCache(true);

            Bitmap viewDrawingCache = iView.getDrawingCache();
            Log.d("GenerateScreen", "Bitmap from View cache is " + viewDrawingCache);

            bitmapFromView = Bitmap.createBitmap(viewDrawingCache);

            Log.d(TAG, "Bitmap created of cache bitmap is " + bitmapFromView);

            iView.setDrawingCacheEnabled(false);

            return bitmapFromView;
        } catch (NullPointerException ne) {
            Log.d(TAG, "" + ne);
        }

        return bitmapFromView;
    }


    /**
     * Save Image Bitmaap to Pictures Directory
     *
     * @param bitmapImage Image to be saved
     * @param fileName    name of the file to be saved
     */
    public void saveToPictures(Bitmap bitmapImage, String fileName) {

        FileOutputStream fos = null;
        File file = null;
        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);

            if (!path.exists())
                //noinspection ResultOfMethodCallIgnored
                path.mkdirs();

            file = new File(path, fileName);

            fos = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 80, fos);
            Toast.makeText(mContext, Constants.SAVE_SUCCESS_MSG, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("TAG", "IOException while saving receipt");
            Toast.makeText(mContext, "Failed to save screen", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }

            // Tell the media scanner about the new file so that it is immediately available to the user.
            if (file != null) {
                MediaScannerConnection.scanFile(mContext,
                        new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.d("ExternalStorage", "Scanned " + path + ":");
                                Log.d("ExternalStorage", "-> uri=" + uri);
                            }
                        });
            }
        }
    }


}
