package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.dao.ReviewDAO;
import com.example.f_food.entity.Review;
import com.example.f_food.R;
import com.example.f_food.repository.ReviewRepository;
import com.example.f_food.screen.authentication_authorization.LoginActivity;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryDetail extends AppCompatActivity {
    private TextView textViewFoodName, textViewFoodDetails, textViewFoodPrice, textViewFoodQuantity;
    private ImageView imageViewFood;

    private int foodId, restaurantId, quantity;
    private String foodName, foodImage ,foodDescription;
    private ReviewDAO reviewDAO;
    private double price;
    private ReviewRepository reviewRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (!isUserLoggedIn()) {
            showAlertDialog("Bạn chưa đăng nhập, bạn vui lòng đăng nhập để thao tác.");
            return;
        }

        // Bind UI components
        /*textViewFoodName = findViewById(R.id.textViewFoodName);
        textViewFoodDetails = findViewById(R.id.textViewFoodDetails);
        //textViewFeedbacks = findViewById(R.id.textViewFeedbacks);
        imageViewFood = findViewById(R.id.imageViewFood);
        //ratingBar = findViewById(R.id.ratingBar);*/
        textViewFoodName = findViewById(R.id.textViewFoodName);
        textViewFoodDetails = findViewById(R.id.textViewFoodDetails);
        textViewFoodPrice = findViewById(R.id.textViewFoodPrice);
        textViewFoodQuantity = findViewById(R.id.textViewFoodQuantity);
        imageViewFood = findViewById(R.id.imageViewFood);


        // Initialize Room Database
        RestaurantRoomDatabase db = RestaurantRoomDatabase.getInstance(this);
        reviewDAO = db.reviewDAO();

        // Get data from Intent
        if (getIntent() != null) {
            foodName = getIntent().getStringExtra("food_name");
            foodImage = getIntent().getStringExtra("food_image");
            foodId = getIntent().getIntExtra("food_id", -1);
            restaurantId = getIntent().getIntExtra("restaurant_id", -1);
            price = getIntent().getDoubleExtra("food_price", 0.0);
            quantity = getIntent().getIntExtra("food_quantity", 1);
            foodDescription = getIntent().getStringExtra("food_description");

            // Set dữ liệu vào UI
            textViewFoodName.setText(foodName);
            textViewFoodPrice.setText("Giá: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(price));
            textViewFoodQuantity.setText("Số lượng: " + quantity);
            textViewFoodDetails.setText("Mô tả: " + foodDescription);

            if (foodImage != null && !foodImage.isEmpty()) {
                Picasso.get().load(foodImage).resize(500, 500).centerCrop().into(imageViewFood);
            }
        }
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("userId", -1); // Sử dụng PreferenceManager thay vì getSharedPreferences
        return userId != -1;
    }

    // Hiển thị hộp thoại thông báo và chuyển sang màn hình đăng nhập
    private void showAlertDialog(String message) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(OrderHistoryDetail.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .create()
                .show();
    }
}
