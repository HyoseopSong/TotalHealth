package com.medichain.totalhealth

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Region
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import com.medichain.totalhealth.databinding.ActivityExamCategoryBinding
import com.medichain.totalhealth.databinding.ActivityHospitalSelectionBinding
import com.medichain.totalhealth.util.BlurBuilder
import com.medichain.totalhealth.view_adapter.HospitalData
import com.medichain.totalhealth.view_adapter.HospitalViewAdapter

class HospitalSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHospitalSelectionBinding
    lateinit var HospitalListAdapter : HospitalViewAdapter
    val HospitalList = ArrayList<HospitalData>()
    var fabSpreaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hospital_selection)

        val onClickItem: View.OnClickListener = View.OnClickListener { v ->
            val mIntent = Intent(this@HospitalSelectionActivity, ExamSelectionActivity::class.java)
            mIntent.putExtra("Category", intent.getStringExtra("Category"))
            startActivity(mIntent)
        }
        val seoul = "서울특별시"
        val busan = "부산광역시"
        val daegu = "대구광역시"
        val inchoen = "인천광역시"
        val kwnagju = "광주광역시"
        val daejun = "대전광역시"
        val ulsan = "울산광역시"
        val sejong = "세종특별자치시"
        val kyungki = "경기도"
        val kangwon = "강원도"
        val chungbook = "충청북도"
        val chungnam = "충청남도"
        val jeonbook = "전라북도"
        val jeonnam = "전라남도"
        val kyungbook = "경상북도"
        val kyungnam = "경상남도"
        val jeju = "제주특별자치도"
        HospitalList.add(HospitalData("강릉아나병원", kangwon))
        HospitalList.add(HospitalData("강원도강릉의료원", kangwon))
        HospitalList.add(HospitalData("강원도원주의료원", kangwon))
        HospitalList.add(HospitalData("강원특별자치도삼척의료원", kangwon))
        HospitalList.add(HospitalData("선한이웃병원", kangwon))
        HospitalList.add(HospitalData("속초의료원", kangwon))
        HospitalList.add(HospitalData("영월의료원", kangwon))
        HospitalList.add(HospitalData("원주성모병원", kangwon))
        HospitalList.add(HospitalData("원주성지병원", kangwon))
        HospitalList.add(HospitalData("원주에이치병원", kangwon))
        HospitalList.add(HospitalData("의료법인삼산의료재단삼산병원", kangwon))
        HospitalList.add(HospitalData("의산의료재단강릉고려종합병원", kangwon))
        HospitalList.add(HospitalData("강릉속편한내과", kangwon))
        HospitalList.add(HospitalData("구철회내과", kangwon))
        HospitalList.add(HospitalData("건누리병원", kyungki))
        HospitalList.add(HospitalData("경기도립의료원이천병원", kyungki))
        HospitalList.add(HospitalData("경기도의료원수원병원", kyungki))
        HospitalList.add(HospitalData("경기도의료원안성병원", kyungki))
        HospitalList.add(HospitalData("경기도의료원의정부병원", kyungki))
        HospitalList.add(HospitalData("경기도의료원포천병원건강증진센터", kyungki))
        HospitalList.add(HospitalData("고려대학교안산병원", kyungki))
        HospitalList.add(HospitalData("구리굿병원", kyungki))
        HospitalList.add(HospitalData("김형근예병원", kyungki))
        HospitalList.add(HospitalData("나리여성병원", kyungki))
        HospitalList.add(HospitalData("남양주국민병원", kyungki))
        HospitalList.add(HospitalData("남양주나눔병원", kyungki))
        HospitalList.add(HospitalData("남양주백병원", kyungki))
        HospitalList.add(HospitalData("남양주엘병원", kyungki))
        HospitalList.add(HospitalData("남양주현대병원", kyungki))
        HospitalList.add(HospitalData("더플러스병원", kyungki))
        HospitalList.add(HospitalData("바른마디병원", kyungki))
        HospitalList.add(HospitalData("본새울병원", kyungki))
        HospitalList.add(HospitalData("봉담나이스병원", kyungki))
        HospitalList.add(HospitalData("부천고운여성병원", kyungki))
        HospitalList.add(HospitalData("부천더만족병원", kyungki))
        HospitalList.add(HospitalData("부천생생병원", kyungki))
        HospitalList.add(HospitalData("부천인본병원", kyungki))
        HospitalList.add(HospitalData("서울나우병원", kyungki))
        HospitalList.add(HospitalData("성남성심병원", kyungki))
        HospitalList.add(HospitalData("송탄중앙병원", kyungki))
        HospitalList.add(HospitalData("수원백성병원", kyungki))
        HospitalList.add(HospitalData("수원버팀병원", kyungki))
        HospitalList.add(HospitalData("수원중앙병원", kyungki))
        HospitalList.add(HospitalData("수원화홍병원", kyungki))
        HospitalList.add(HospitalData("시대병원", kyungki))
        HospitalList.add(HospitalData("안양윌스기념병원", kyungki))
        HospitalList.add(HospitalData("에이비씨병원", kyungki))
        HospitalList.add(HospitalData("여주세종병원", kyungki))
        HospitalList.add(HospitalData("여주연세새로운병원", kyungki))
        HospitalList.add(HospitalData("연세오케이병원", kyungki))
        HospitalList.add(HospitalData("영동의료재단의정부백병원", kyungki))
        HospitalList.add(HospitalData("예손병원", kyungki))
        HospitalList.add(HospitalData("오산버팀병원", kyungki))
        HospitalList.add(HospitalData("오정본병원", kyungki))
        HospitalList.add(HospitalData("오케이참병원", kyungki))
        HospitalList.add(HospitalData("윤서병원", kyungki))
        HospitalList.add(HospitalData("의료법인혜수의료재단베리굿병원", kyungki))
        HospitalList.add(HospitalData("이천바른병원", kyungki))
        HospitalList.add(HospitalData("인제대일산백병원", kyungki))
        HospitalList.add(HospitalData("정병원", kyungki))
        HospitalList.add(HospitalData("진접한양병원", kyungki))
        HospitalList.add(HospitalData("파주성모요양병원", kyungki))
        HospitalList.add(HospitalData("파주인본병원", kyungki))
        HospitalList.add(HospitalData("평촌우리병원", kyungki))
        HospitalList.add(HospitalData("평택서울제일병원", kyungki))
        HospitalList.add(HospitalData("한양대학교구리병원", kyungki))
        HospitalList.add(HospitalData("호원병원", kyungki))
        HospitalList.add(HospitalData("화성디에스병원", kyungki))
        HospitalList.add(HospitalData("21세기연세의원(정자)", kyungki))
        HospitalList.add(HospitalData("SOK속편한내과(파주)", kyungki))
        HospitalList.add(HospitalData("강내과", kyungki))
        HospitalList.add(HospitalData("건강한미래항외과의원", kyungki))
        HospitalList.add(HospitalData("광교내과", kyungki))
        HospitalList.add(HospitalData("광교윌내과건강검진센터", kyungki))
        HospitalList.add(HospitalData("남양주강내과의원", kyungki))
        HospitalList.add(HospitalData("뉴선두연합내과", kyungki))
        HospitalList.add(HospitalData("리체내과신경과의원", kyungki))
        HospitalList.add(HospitalData("베스트가정의학과의원", kyungki))
        HospitalList.add(HospitalData("베스트내과(포곡)", kyungki))
        HospitalList.add(HospitalData("봄여성의원", kyungki))
        HospitalList.add(HospitalData("부천SOK속편한내과의원", kyungki))
        HospitalList.add(HospitalData("분당21세기의원(서현)", kyungki))
        HospitalList.add(HospitalData("분당에이치의원", kyungki))
        HospitalList.add(HospitalData("서안성의원", kyungki))
        HospitalList.add(HospitalData("서울연합내과의원", kyungki))
        HospitalList.add(HospitalData("서울황제내과의원", kyungki))
        HospitalList.add(HospitalData("성모플러스내과소아과의원", kyungki))
        HospitalList.add(HospitalData("속편한내과의원(산본)", kyungki))
        HospitalList.add(HospitalData("송승찬내과", kyungki))
        HospitalList.add(HospitalData("송정길내과의원", kyungki))
        HospitalList.add(HospitalData("수지미래산부인과", kyungki))
        HospitalList.add(HospitalData("스마일내과의원", kyungki))
        HospitalList.add(HospitalData("안산의료사협새안산의원", kyungki))
        HospitalList.add(HospitalData("안산제일조은내과의원", kyungki))
        HospitalList.add(HospitalData("안성농민의원", kyungki))
        HospitalList.add(HospitalData("안성의료복지사회적협동조합새봄치과의원", kyungki))
        HospitalList.add(HospitalData("안양속편한내과", kyungki))
        HospitalList.add(HospitalData("양주아산장편한내과", kyungki))
        HospitalList.add(HospitalData("여주아산내과의원", kyungki))
        HospitalList.add(HospitalData("여주영상의학과의원", kyungki))
        HospitalList.add(HospitalData("예일내과", kyungki))
        HospitalList.add(HospitalData("온내과의원", kyungki))
        HospitalList.add(HospitalData("용인아산병원", kyungki))
        HospitalList.add(HospitalData("의정부속편한내과", kyungki))
        HospitalList.add(HospitalData("이준편한내과의원", kyungki))
        HospitalList.add(HospitalData("거제맑은샘병원", kyungnam))
        HospitalList.add(HospitalData("메가병원", kyungnam))
        HospitalList.add(HospitalData("상남굿모닝내과병원", kyungnam))
        HospitalList.add(HospitalData("진주본병원", kyungnam))
        HospitalList.add(HospitalData("창원에스엠지연세병원", kyungnam))
        HospitalList.add(HospitalData("창원파티마병원", kyungnam))
        HospitalList.add(HospitalData("창원한마음병원", kyungnam))
        HospitalList.add(HospitalData("통영고려병원", kyungnam))
        HospitalList.add(HospitalData("통영서울병원", kyungnam))
        HospitalList.add(HospitalData("함양성심병원", kyungnam))
        HospitalList.add(HospitalData("희망병원", kyungnam))
        HospitalList.add(HospitalData("거창종합내과", kyungnam))
        HospitalList.add(HospitalData("참조은산부인과", kyungnam))
        HospitalList.add(HospitalData("경상북도안동의료원", kyungbook))
        HospitalList.add(HospitalData("새천년병원", kyungbook))
        HospitalList.add(HospitalData("에스포항병원", kyungbook))
        HospitalList.add(HospitalData("울진군의료원", kyungbook))
        HospitalList.add(HospitalData("포항시티병원", kyungbook))
        HospitalList.add(HospitalData("포항여성병원", kyungbook))
        HospitalList.add(HospitalData("포항여성아이병원", kyungbook))
        HospitalList.add(HospitalData("강기훈의당신내과의원", kyungbook))
        HospitalList.add(HospitalData("더속편한내과의원", kyungbook))
        HospitalList.add(HospitalData("삼성연합내과의원", kyungbook))
        HospitalList.add(HospitalData("세명영상의학과의원", kyungbook))
        HospitalList.add(HospitalData("광주SKJ병원", kwnagju))
        HospitalList.add(HospitalData("광주북구미래아동병원", kwnagju))
        HospitalList.add(HospitalData("광주서광병원", kwnagju))
        HospitalList.add(HospitalData("광주수완미래아동병원", kwnagju))
        HospitalList.add(HospitalData("광주수완병원", kwnagju))
        HospitalList.add(HospitalData("동명병원", kwnagju))
        HospitalList.add(HospitalData("태영21내과의원", kwnagju))
        HospitalList.add(HospitalData("나사렛종합병원", daegu))
        HospitalList.add(HospitalData("대구광개토병원", daegu))
        HospitalList.add(HospitalData("대구구병원", daegu))
        HospitalList.add(HospitalData("대구기독병원", daegu))
        HospitalList.add(HospitalData("대구라파엘", daegu))
        HospitalList.add(HospitalData("대구삼일병원", daegu))
        HospitalList.add(HospitalData("대구시티병원", daegu))
        HospitalList.add(HospitalData("대구여성메디파크병원", daegu))
        HospitalList.add(HospitalData("드림병원", daegu))
        HospitalList.add(HospitalData("미래여성병원", daegu))
        HospitalList.add(HospitalData("서대구병원", daegu))
        HospitalList.add(HospitalData("아주조은요양병원", daegu))
        HospitalList.add(HospitalData("영남대학교병원", daegu))
        HospitalList.add(HospitalData("푸른병원", daegu))
        HospitalList.add(HospitalData("강미정연합내과의원", daegu))
        HospitalList.add(HospitalData("나사렛연합내과의원", daegu))
        HospitalList.add(HospitalData("대구속안심내과", daegu))
        HospitalList.add(HospitalData("대구이앤김연합내과", daegu))
        HospitalList.add(HospitalData("대한내과의원", daegu))
        HospitalList.add(HospitalData("속시원이재성내과의원", daegu))
        HospitalList.add(HospitalData("중앙기업의원", daegu))
        HospitalList.add(HospitalData("킴스건강내과외과의원", daegu))
        HospitalList.add(HospitalData("푸른미래내과", daegu))
        HospitalList.add(HospitalData("대전글로벌튼튼병원", daejun))
        HospitalList.add(HospitalData("대전한국병원", daejun))
        HospitalList.add(HospitalData("의료법인대전병원", daejun))
        HospitalList.add(HospitalData("가오플러스산부인과", daejun))
        HospitalList.add(HospitalData("대전아이든소아청소년과", daejun))
        HospitalList.add(HospitalData("데일리연합의원", daejun))
        HospitalList.add(HospitalData("둔산속편한내과·영상의학과", daejun))
        HospitalList.add(HospitalData("미소가득한내과의원", daejun))
        HospitalList.add(HospitalData("민들레의료복지사회적협동조합민들레검진센터", daejun))
        HospitalList.add(HospitalData("서대전속편한내과", daejun))
        HospitalList.add(HospitalData("세계영상의학과의원", daejun))
        HospitalList.add(HospitalData("송강복음내과의원", daejun))
        HospitalList.add(HospitalData("우리수내과의원", daejun))
        HospitalList.add(HospitalData("카이스트부속의원", daejun))
        HospitalList.add(HospitalData("코스모내과종합검진센터", daejun))
        HospitalList.add(HospitalData("하나로의원", daejun))
        HospitalList.add(HospitalData("강동병원", busan))
        HospitalList.add(HospitalData("고려병원", busan))
        HospitalList.add(HospitalData("대한병원", busan))
        HospitalList.add(HospitalData("부산광역시의료원", busan))
        HospitalList.add(HospitalData("부산웰니스병원", busan))
        HospitalList.add(HospitalData("부산큰병원", busan))
        HospitalList.add(HospitalData("부산한사랑내과병원", busan))
        HospitalList.add(HospitalData("부산휴병원", busan))
        HospitalList.add(HospitalData("상쾌한병원", busan))
        HospitalList.add(HospitalData("새우리남산병원", busan))
        HospitalList.add(HospitalData("세흥병원", busan))
        HospitalList.add(HospitalData("연제일신병원", busan))
        HospitalList.add(HospitalData("인제대부산백병원", busan))
        HospitalList.add(HospitalData("인제대해운대백병원", busan))
        HospitalList.add(HospitalData("일신기독병원", busan))
        HospitalList.add(HospitalData("정관일신기독병원", busan))
        HospitalList.add(HospitalData("하단프라임병원", busan))
        HospitalList.add(HospitalData("김계영내과", busan))
        HospitalList.add(HospitalData("부산원광메디컬의원", busan))
        HospitalList.add(HospitalData("강남새로운병원", seoul))
        HospitalList.add(HospitalData("강동성심병원", seoul))
        HospitalList.add(HospitalData("강북21세기병원", seoul))
        HospitalList.add(HospitalData("건국대학교병원", seoul))
        HospitalList.add(HospitalData("고려대학교구로병원", seoul))
        HospitalList.add(HospitalData("고려대학교안암병원", seoul))
        HospitalList.add(HospitalData("광혜병원", seoul))
        HospitalList.add(HospitalData("국립재활원", seoul))
        HospitalList.add(HospitalData("국립정신건강센터", seoul))
        HospitalList.add(HospitalData("기쁨병원", seoul))
        HospitalList.add(HospitalData("대항병원", seoul))
        HospitalList.add(HospitalData("마포연세한강병원", seoul))
        HospitalList.add(HospitalData("메디힐병원", seoul))
        HospitalList.add(HospitalData("민트병원", seoul))
        HospitalList.add(HospitalData("반도정형외과병원", seoul))
        HospitalList.add(HospitalData("사랑의병원", seoul))
        HospitalList.add(HospitalData("서울9988병원", seoul))
        HospitalList.add(HospitalData("서울유병원", seoul))
        HospitalList.add(HospitalData("서울프라임병원", seoul))
        HospitalList.add(HospitalData("서울프라임병원", seoul))
        HospitalList.add(HospitalData("서울현대병원", seoul))
        HospitalList.add(HospitalData("선한목자병원", seoul))
        HospitalList.add(HospitalData("에이치큐브병원", seoul))
        HospitalList.add(HospitalData("연세슬기병원", seoul))
        HospitalList.add(HospitalData("올바른서울병원", seoul))
        HospitalList.add(HospitalData("웰튼병원", seoul))
        HospitalList.add(HospitalData("인정병원", seoul))
        HospitalList.add(HospitalData("인제대상계백병원", seoul))
        HospitalList.add(HospitalData("조은마디병원", seoul))
        HospitalList.add(HospitalData("청구성심병원", seoul))
        HospitalList.add(HospitalData("청담튼튼병원", seoul))
        HospitalList.add(HospitalData("청병원", seoul))
        HospitalList.add(HospitalData("포미즈여성병원", seoul))
        HospitalList.add(HospitalData("하늘병원", seoul))
        HospitalList.add(HospitalData("한양대학교서울병원", seoul))
        HospitalList.add(HospitalData("LBO의원", seoul))
        HospitalList.add(HospitalData("가야내과검진센터", seoul))
        HospitalList.add(HospitalData("강남탑내과의원", seoul))
        HospitalList.add(HospitalData("강동서울의원", seoul))
        HospitalList.add(HospitalData("강동천호내과", seoul))
        HospitalList.add(HospitalData("강북으뜸내과", seoul))
        HospitalList.add(HospitalData("건강미소내과", seoul))
        HospitalList.add(HospitalData("고려가정의학과의원", seoul))
        HospitalList.add(HospitalData("국민건강내과의원", seoul))
        HospitalList.add(HospitalData("국민체력센터의원", seoul))
        HospitalList.add(HospitalData("독일하트의원", seoul))
        HospitalList.add(HospitalData("메디넷영동의원", seoul))
        HospitalList.add(HospitalData("메디휴제일내과", seoul))
        HospitalList.add(HospitalData("명진단영상의학과", seoul))
        HospitalList.add(HospitalData("미래아이산부인과,내과", seoul))
        HospitalList.add(HospitalData("새서울연합의원", seoul))
        HospitalList.add(HospitalData("서울내외의원", seoul))
        HospitalList.add(HospitalData("서울메디투어의원", seoul))
        HospitalList.add(HospitalData("서울성모내과", seoul))
        HospitalList.add(HospitalData("서울아산내과의원", seoul))
        HospitalList.add(HospitalData("서울장문외과", seoul))
        HospitalList.add(HospitalData("시원누리내과", seoul))
        HospitalList.add(HospitalData("신항외과의원", seoul))
        HospitalList.add(HospitalData("아셈내과의원", seoul))
        HospitalList.add(HospitalData("어광수가정의학과", seoul))
        HospitalList.add(HospitalData("염문선내과", seoul))
        HospitalList.add(HospitalData("올리브내과", seoul))
        HospitalList.add(HospitalData("유엔장내과", seoul))
        HospitalList.add(HospitalData("인터케어내과의원", seoul))
        HospitalList.add(HospitalData("전자랜드클리닉센터:전자랜드의원", seoul))
        HospitalList.add(HospitalData("제이내과", seoul))
        HospitalList.add(HospitalData("중계윌내과", seoul))
        HospitalList.add(HospitalData("참좋은내과의원", seoul))
        HospitalList.add(HospitalData("청담H내과종합검진센터", seoul))
        HospitalList.add(HospitalData("큰길내과의원", seoul))
        HospitalList.add(HospitalData("한국의료재단IFC의원", seoul))
        HospitalList.add(HospitalData("해성산부인과의원", seoul))
        HospitalList.add(HospitalData("햇살가득내과의원", seoul))
        HospitalList.add(HospitalData("홍제속편한내과의원", seoul))
        HospitalList.add(HospitalData("휴먼영상의학과의원", seoul))
        HospitalList.add(HospitalData("세종드림연합내과", sejong))
        HospitalList.add(HospitalData("서울산보람병원", ulsan))
        HospitalList.add(HospitalData("울산보람병원", ulsan))
        HospitalList.add(HospitalData("의)고담의료재단마더스병원", ulsan))
        HospitalList.add(HospitalData("동울산영상의학과의원", ulsan))
        HospitalList.add(HospitalData("신내과", ulsan))
        HospitalList.add(HospitalData("바로병원", inchoen))
        HospitalList.add(HospitalData("인천전병원", inchoen))
        HospitalList.add(HospitalData("플러스병원", inchoen))
        HospitalList.add(HospitalData("한마음병원", inchoen))
        HospitalList.add(HospitalData("송도플러스정형외과의원", inchoen))
        HospitalList.add(HospitalData("우리들내과의원", inchoen))
        HospitalList.add(HospitalData("인천계양속편한내과", inchoen))
        HospitalList.add(HospitalData("인천속편한내과의원(구월동)", inchoen))
        HospitalList.add(HospitalData("정내과의원", inchoen))
        HospitalList.add(HospitalData("광양서울병원", jeonnam))
        HospitalList.add(HospitalData("나주NH미래아동병원", jeonnam))
        HospitalList.add(HospitalData("녹동현대병원", jeonnam))
        HospitalList.add(HospitalData("담양사랑병원", jeonnam))
        HospitalList.add(HospitalData("목포미래병원", jeonnam))
        HospitalList.add(HospitalData("목포세안종합병원", jeonnam))
        HospitalList.add(HospitalData("무안병원", jeonnam))
        HospitalList.add(HospitalData("삼호의료재단벌교삼호병원", jeonnam))
        HospitalList.add(HospitalData("순천순정병원", jeonnam))
        HospitalList.add(HospitalData("순천에스병원", jeonnam))
        HospitalList.add(HospitalData("순천중앙병원", jeonnam))
        HospitalList.add(HospitalData("순천평화병원", jeonnam))
        HospitalList.add(HospitalData("순천하나병원", jeonnam))
        HospitalList.add(HospitalData("순천한국병원", jeonnam))
        HospitalList.add(HospitalData("순천희망병원", jeonnam))
        HospitalList.add(HospitalData("여수전남병원", jeonnam))
        HospitalList.add(HospitalData("여천전남병원", jeonnam))
        HospitalList.add(HospitalData("영광기독병원", jeonnam))
        HospitalList.add(HospitalData("장흥종합병원", jeonnam))
        HospitalList.add(HospitalData("전라남도강진의료원", jeonnam))
        HospitalList.add(HospitalData("플러스아이미코", jeonnam))
        HospitalList.add(HospitalData("해남우리종합병원", jeonnam))
        HospitalList.add(HospitalData("남악온누리내과의원", jeonnam))
        HospitalList.add(HospitalData("순천드림내과의원", jeonnam))
        HospitalList.add(HospitalData("순천플러스내과", jeonnam))
        HospitalList.add(HospitalData("장성중앙내과의원", jeonnam))
        HospitalList.add(HospitalData("군산성신병원", jeonbook))
        HospitalList.add(HospitalData("남원의료원", jeonbook))
        HospitalList.add(HospitalData("부안성모병원", jeonbook))
        HospitalList.add(HospitalData("의료법인유진의료재단동서병원", jeonbook))
        HospitalList.add(HospitalData("호성전주병원", jeonbook))
        HospitalList.add(HospitalData("전주온누리연합내과의원", jeonbook))
        HospitalList.add(HospitalData("제주S중앙병원", jeju))
        HospitalList.add(HospitalData("제주의료원", jeju))
        HospitalList.add(HospitalData("제주특별자치도서귀포의료원", jeju))
        HospitalList.add(HospitalData("표선연세의원", jeju))
        HospitalList.add(HospitalData("곡성사랑병원", chungnam))
        HospitalList.add(HospitalData("당진9988병원", chungnam))
        HospitalList.add(HospitalData("당진종합병원", chungnam))
        HospitalList.add(HospitalData("부여성요셉병원", chungnam))
        HospitalList.add(HospitalData("서산중앙병원", chungnam))
        HospitalList.add(HospitalData("아산충무병원", chungnam))
        HospitalList.add(HospitalData("아산현대병원", chungnam))
        HospitalList.add(HospitalData("예산명지병원", chungnam))
        HospitalList.add(HospitalData("예산종합병원", chungnam))
        HospitalList.add(HospitalData("예일병원", chungnam))
        HospitalList.add(HospitalData("이화병원", chungnam))
        HospitalList.add(HospitalData("천안다나힐병원", chungnam))
        HospitalList.add(HospitalData("천안우리병원", chungnam))
        HospitalList.add(HospitalData("천안혜강병원", chungnam))
        HospitalList.add(HospitalData("김용현내과의원", chungnam))
        HospitalList.add(HospitalData("다나메디피아", chungnam))
        HospitalList.add(HospitalData("박태욱미소내과", chungnam))
        HospitalList.add(HospitalData("서산조은내과의원", chungnam))
        HospitalList.add(HospitalData("아산제일내과의원", chungnam))
        HospitalList.add(HospitalData("아산제일조은내과", chungnam))
        HospitalList.add(HospitalData("오유경내과의원", chungnam))
        HospitalList.add(HospitalData("장사랑연합내과", chungnam))
        HospitalList.add(HospitalData("제일편한내과의원", chungnam))
        HospitalList.add(HospitalData("천안속편한내과", chungnam))
        HospitalList.add(HospitalData("천안엔도내과", chungnam))
        HospitalList.add(HospitalData("천안이수내과의원", chungnam))
        HospitalList.add(HospitalData("천안충무병원", chungnam))
        HospitalList.add(HospitalData("괴산성모병원", chungbook))
        HospitalList.add(HospitalData("씨앤씨푸른병원", chungbook))
        HospitalList.add(HospitalData("옥천성모병원", chungbook))
        HospitalList.add(HospitalData("음성제일조은병원", chungbook))
        HospitalList.add(HospitalData("진천중앙제일병원", chungbook))
        HospitalList.add(HospitalData("청주마이크로병원", chungbook))
        HospitalList.add(HospitalData("청주베스티안병원", chungbook))
        HospitalList.add(HospitalData("청주프라임병원", chungbook))
        HospitalList.add(HospitalData("충북영동병원", chungbook))
        HospitalList.add(HospitalData("충주김앤권병원", chungbook))
        HospitalList.add(HospitalData("연세리더스김재권내과", chungbook))
        HospitalList.add(HospitalData("청주김내과", chungbook))
        HospitalList.add(HospitalData("청주장사랑박종범내과", chungbook))
        HospitalList.add(HospitalData("충북음성베스트의원", chungbook))
        HospitalListAdapter = HospitalViewAdapter(this@HospitalSelectionActivity, HospitalList, onClickItem)
        binding.examCategoryListRecyclerView.adapter = HospitalListAdapter
        HospitalListAdapter.filter.filter(daegu)
        title = daegu

        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        binding.fabFront.setOnClickListener{
            if(fabSpreaded){
                compressFabs()
            }else{
                spreadFabs()
            }
        }

        binding.fabView.setOnClickListener {
            compressFabs()
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(fabSpreaded){
                compressFabs()
            }else{
                finish()
            }
        }
    }
    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

        if(it.resultCode == 1001){
            val selectedRegion = it.data!!.getStringExtra("regionString")
            HospitalListAdapter.filter.filter(selectedRegion)
            title = selectedRegion
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.hospital_selection, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filtering -> {
                if(fabSpreaded) {
                    compressFabs()
                } else {
                    val mIntent = Intent(this@HospitalSelectionActivity, RegionChoiceActivity::class.java)
                    mIntent.putExtra("selected", title)
                    activityResultLauncher.launch(mIntent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun spreadFabs(){
        binding.fabFront.visibility = View.INVISIBLE
        val bitmap = this.window.decorView.rootView.drawToBitmap(Bitmap.Config.ARGB_8888)
        val blurBuilder = BlurBuilder()
        binding.fabView.setImageBitmap(blurBuilder.blur(this@HospitalSelectionActivity, bitmap))

        binding.fabFront.visibility = View.VISIBLE
        binding.fabView.visibility = View.VISIBLE
        binding.fabCaption1.visibility = View.VISIBLE
        binding.fabCaption2.visibility = View.VISIBLE
        binding.fabCaption3.visibility = View.VISIBLE
//        binding.fabCaption4.visibility = View.VISIBLE
//        binding.fabCaption5.visibility = View.VISIBLE
//        binding.fabCaption6.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(binding.fabLocation, "translationY", -200f).apply { start() }
        ObjectAnimator.ofFloat(binding.fabHangul, "translationY", -380f).apply { start() }
        ObjectAnimator.ofFloat(binding.fabSearch, "translationY", -560f).apply { start() }
//        ObjectAnimator.ofFloat(binding.fabAddCorp, "translationY", -560f).apply { start() }
//        ObjectAnimator.ofFloat(binding.fabAddScrap, "translationY", -740f).apply { start() }
//        ObjectAnimator.ofFloat(binding.fabSelvy, "translationY", -920f).apply { start() }
//        ObjectAnimator.ofFloat(binding.fabDirectInsure, "translationY", -1100f).apply { start() }
        binding.fabFront.setImageDrawable(AppCompatResources.getDrawable(this@HospitalSelectionActivity, R.drawable.ggob_pyo))
        fabSpreaded = true
    }

    fun compressFabs(){
        binding.fabView.visibility = View.GONE
        binding.fabCaption1.visibility = View.GONE
        binding.fabCaption2.visibility = View.GONE
        binding.fabCaption3.visibility = View.GONE
//        binding.fabCaption4.visibility = View.GONE
//        binding.fabCaption5.visibility = View.GONE
//        binding.fabCaption6.visibility = View.GONE
        ObjectAnimator.ofFloat(binding.fabLocation, "translationY", 0f).apply { start() }
        ObjectAnimator.ofFloat(binding.fabHangul, "translationY", 0f).apply { start() }
//        ObjectAnimator.ofFloat(binding.fabAddCorp, "translationY", 0f).apply { start() }
//        ObjectAnimator.ofFloat(binding.fabAddScrap, "translationY", 0f).apply { start() }
        ObjectAnimator.ofFloat(binding.fabSearch, "translationY", 0f).apply { start() }
//        ObjectAnimator.ofFloat(binding.fabDirectInsure, "translationY", 0f).apply { start() }
        binding.fabFront.setImageDrawable(AppCompatResources.getDrawable(this@HospitalSelectionActivity, R.drawable.more_vertical))
        fabSpreaded = false
    }
}