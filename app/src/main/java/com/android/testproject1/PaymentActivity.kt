package com.android.testproject1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.databinding.ActivityPaymentBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.android.testproject1.viewmodels.PaymentActivityViewModel
import com.google.android.material.button.MaterialButton

class PaymentActivity : AppCompatActivity(),IMainActivity {

    private lateinit var mViewModel: PaymentActivityViewModel
    private lateinit var binding: ActivityPaymentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val groupItem= intent.getParcelableExtra<UsersChatListEntity>("groupItem")

        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment)

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(this.application))
            .get(PaymentActivityViewModel::class.java)

        if (groupItem != null) {
            mViewModel.loadGroupMembers(groupItem.createdBy,groupItem.postId)
        }

        mViewModel.getGroupList().observe(this, {
            binding.dataList= it
        })

//        val chart = binding.chart1
////        chart.setUsePercentValues(true)
//        chart.description.isEnabled = false
//        chart.setExtraOffsets(2F, 4F, 2F, 2F)
//
//        chart.dragDecelerationFrictionCoef = 0.95f
//
////        chart.setCenterTextTypeface(tfLight)
//        chart.centerText = "Payments"
//
//        chart.isDrawHoleEnabled = true
////        chart.setHoleColor(Color.WHITE)
//
////        chart.setTransparentCircleColor(Color.WHITE)
//        chart.setTransparentCircleAlpha(110)
//
//        chart.holeRadius = 58f
////        chart.transparentCircleRadius = 61f
//
//        chart.setDrawCenterText(true)
//
//        chart.rotationAngle = 0F
//        // enable rotation of the chart by touch
//        // enable rotation of the chart by touch
//        chart.isRotationEnabled = true
//        chart.isHighlightPerTapEnabled = true
//
//        // chart.setUnit(" €");
//        // chart.setDrawUnitsInChart(true);
//
//        // add a selection listener
//
//        // chart.setUnit(" €");
//        // chart.setDrawUnitsInChart(true);
//
//        // add a selection listener
//        chart.setOnChartValueSelectedListener(this)
//
////        seekBarX.setProgress(4)
////        seekBarY.setProgress(10)
//
//        chart.animateY(1400, Easing.EaseInOutQuad)
//        // chart.spin(2000, 0, 360);
//
//        // chart.spin(2000, 0, 360);
////        val l: Legend = chart.legend
////        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
////        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
////        l.orientation = Legend.LegendOrientation.VERTICAL
////        l.setDrawInside(false)
////        l.xEntrySpace = 7f
////        l.yEntrySpace = 0f
////        l.yOffset = 0f
//
//        // entry label styling
//
//        // entry label styling
//        chart.setEntryLabelColor(Color.WHITE)
////        chart.setEntryLabelTypeface(tfRegular)
//        chart.setEntryLabelTextSize(20f)






//        circularProgressBar.apply {
//            // Set Progress
////            progress = 65f
//            // or with animation
//            setProgressWithAnimation(2000f, 2000) // =1s
//
//            // Set Progress Max
//            progressMax = 5000f
//
//            // Set ProgressBar Color
////            progressBarColor = Color.BLACK
//            // or with gradient
//            progressBarColorStart = Color.RED
//            progressBarColorEnd = Color.WHITE
//            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
//
//            // Set background ProgressBar Color
////            backgroundProgressBarColor = Color.GRAY
//            // or with gradient
//            backgroundProgressBarColorStart = Color.WHITE
//            backgroundProgressBarColorEnd = Color.WHITE
//            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
//
//            // Set Width
//            progressBarWidth = 14f // in DP
//            backgroundProgressBarWidth = 3f // in DP
//
//            // Other
//            roundBorder = true
//            startAngle = 0f
//            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
//        }

//        setData(5,5000F)

    }

//    private fun setData(count: Int, range: Float) {
//        val entries: ArrayList<PieEntry> = ArrayList()
//
//        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
//        // the chart.
////        parties.get(i % parties.length),
////        for (i in 0 until count) {
////            entries.add(PieEntry((Math.random() * range + range / 5).toFloat(),
////                    resources.getDrawable(R.drawable.star)
////                )
////            )
////        }
//
//        entries.add(PieEntry(2000F))
//        entries.add(PieEntry(500F))
//        entries.add(PieEntry(500F))
//        entries.add(PieEntry(1000F))
//        entries.add(PieEntry(1000F))
//        val dataSet = PieDataSet(entries,"")
//        dataSet.setDrawIcons(false)
//        dataSet.sliceSpace = 3f
//        dataSet.iconsOffset = MPPointF(0F, 48F)
//        dataSet.selectionShift = 5f
//
//        // add a lot of colors
//        val colors: ArrayList<Int> = ArrayList()
//        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
//        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
//        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
//        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
//        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
//        colors.add(ColorTemplate.getHoloBlue())
//        dataSet.colors = colors
//        //dataSet.setSelectionShift(0f);
//        val data = PieData(dataSet)
//        data.setValueFormatter(PercentFormatter())
//        data.setValueTextSize(14f)
//        data.setValueTextColor(Color.WHITE)
////        data.setValueTypeface(tfLight)
//        binding.chart1.data = data
//
//        // undo all highlights
//        binding.chart1.highlightValues(null)
//        binding.chart1.invalidate()
//    }

    override fun onRegisterClick() {
        TODO("Not yet implemented")
    }

    override fun onRecyclerViewGroupsItemClick(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onPlaceOrderClick(offerItem: OfferRoomEntity, Total: String, Quantity: String) {
        TODO("Not yet implemented")
    }

    override fun onAcceptClick(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        reject: MaterialButton,
        accept: MaterialButton,
        cancel: MaterialButton,
        ready: MaterialButton,
        linearLayout: LinearLayout,
        linearlayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearlayout4: LinearLayout
    ) {
        TODO("Not yet implemented")
    }

    override fun onOrderReadyClick(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        cancel: MaterialButton,
        ready: MaterialButton,
        waiting: MaterialButton,
        linearLayout: LinearLayout,
        linearLayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearLayout4: LinearLayout
    ) {
        TODO("Not yet implemented")
    }

    override fun onWaitingClicked(
        notificationItem: NotificationsRoomEntity,
        linearLayout: LinearLayout,
        linearLayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearLayout4: LinearLayout
    ) {
        TODO("Not yet implemented")
    }

    override fun onCurrentUserOfferClick(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onProfileOpenedDiscover(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onJoinItemClick(userItem: UsersChatListEntity) {
        TODO("Not yet implemented")
    }

    override fun onGroupItemClick(userItem: UsersChatListEntity) {
        TODO("Not yet implemented")
    }

    override fun onGroupItemClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onLocationClick() {
        TODO("Not yet implemented")
    }

    override fun onSendFriendReqClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onNotificationItemClick(notificationsRoomEntityItem: NotificationsRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onSearchPeopleItemClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onBookMarkItemClick(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onGroupOpenFromMessages(userItem: UsersChatListEntity) {
        TODO("Not yet implemented")
    }

//    override fun onValueSelected(e: Entry?, h: Highlight?) {
//
//    }
//
//    override fun onNothingSelected() {
//    }
}