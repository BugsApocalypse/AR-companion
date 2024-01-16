package com.adityagupta.arcompanion.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.Utils
import com.adityagupta.arcompanion.databinding.ActivityImageDisplayBinding
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText


class ImageDisplayActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityImageDisplayBinding
    var modelList = mutableListOf<String>("ambulance","apple","apple-half","avocado","avocado-half","axe","bacon","bacon-raw","bag","bag-flat","banana","barn","barrel","bed","beet","bench","bike","blaster-a","boat-large","boat-small","bottle","bottle-ketchup","bottle-large","bottle-musterd","bottle-oil","bowl","bowl-broth","bowl-cereal","bowl-soup","bread","bridge-01","broccoli","bunny","burger","burger-cheese","burger-cheese-double","burger-double","cabbage","cactus","cake","cake-birthday","cake-slicer","can","can-open","can-small","candy-bar","candy-bar-wrapper","cannon","cannon-ball","cannon-large","cannon-mobile","carrot","carton","carton-small","cauliflower","celery-stick","cheese","cheese-cut","cheese-slicer","cherries","chest","chinese","chocolate","chocolate-wrapper","chopstick","chopstick-fancy","citroen-old-van","cocktail","coconut","coconut-half","cookie","cookie-chocolate","cooking-fork","cooking-knife","cooking-knife-choppi","cooking-spatula","cooking-spoon","corn","corn-dog","coronavirus","croissant","cup","cup-saucer","cup-tea","cup-thea","cupcake","cutting-board","cutting-board-japane","cutting-board-round","cyborg-female","delivery-truck","dim-sum","dogue","donut","donut-1","donut-chocolate","donut-sprinkles","dragon","egg","egg-cooked","egg-cup","egg-half","eggplant","expresso-cup","ferris-wheel","firetruck","fish","fish-bones","formation-large-rock","formation-large-ston","formation-rock","formation-stone","frappe","fries","fries-empty","frikandel-speciaal","frying-pan","frying-pan-lid","garbage-truck","ginger-bread","ginger-bread-cutter","glass","glass-wine","grapes","guitar","hatchback","headphones","helicopter","hole","honey","hot-dog","hot-dog-raw","house-18","house-3","house-4","house-5","house-6","house-7","house1","ice-cream","ice-cream-cone","ice-cream-cup","ice-cream-scoop","ice-cream-scoop-mint","ice-cream-truck","knife-block","leek","lemon","lemon-half","les-paul","library-large","loaf","loaf-baguette","loaf-round","lollypop","low-poly-farm","low-poly-horse","low-poly-tree","lucy","maki-roe","maki-salmon","maki-vegetable","male","meat-cooked","meat-patty","meat-raw","meat-ribs","meat-sausage","meat-tenderizer","mechanical-keyboard","mechanical-keyboard-","mincemeat-pie","mortar","mortar-pestle","muffin","mug","mug-1","mushroom","mushroom-half","mussel","mussel-open","nes","nes-controller","node","onion","onion-half","orange","paddle","palm-detailed-long","palm-detailed-short","palm-long","palm-short","pan","pan-stew","pancakes","paprika","paprika-slice","peanut-butter","pear","pear-half","pepper","pepper-mill","pie","pillow","pineapple","pirate-captain","pirate-crew","pirate-officer","pizza","pizza-box","pizza-cutter","plant","plant-pirate-kit","plate","plate-broken","plate-deep","plate-dinner","plate-rectangle","plate-sauerkraut","police-car","popsicle","popsicle-chocolate","popsicle-stick","pot","pot-lid","pot-stew","pot-stew-lid","present","pudding","pumpkin","pumpkin-basic","race-car","race-futrure","radish","react-logo","rice-ball","rolling-pin","salad","sandwich","sausage","sausage-half","sci-fi-crate","sedan","shaker-pepper","shaker-salt","ship-dark","ship-light","ship-wreck","shovel","skater-female","skater-male","skewer","skewer-vegetables","soda","soda-bottle","soda-can","soda-can-crushed","soda-glass","soy","spilling-coffee","sports-sedan","steamer","strawberry","styrofoam","styrofoam-dinner","sub","sundae","survivor-female","survivor-male","sushi-egg","sushi-roll","sushi-salmon","suv","suv-luxury","suzanne-high-poly","suzanne-low-poly","sword","sword-scimitar","table","taco","tajine","tajine-lid","taxi","tomato","tomato-slice","tower","tractor","tractor-2","tractor-police","tree-big","tree-small","truck","truck-flat","turkey","turntable","utensil-fork","utensil-knife","utensil-spoon","van","waffle","watermelon","whipped-cream","whisk","whole-ham","wholer-ham","wind-turbine","wine-red","wine-white","zombie-1","zombie-2")

    var flag = 0
    lateinit var image: Bitmap
    var words = mutableListOf<String>()
    var coords = mutableListOf<Rect>()
    var selectedWord = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)

        viewBinding = ActivityImageDisplayBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val st = intent.getStringExtra("path")!!
        val t = Uri.parse(st)
        image = MediaStore.Images.Media.getBitmap(contentResolver, t)


        image = rotateImage(image, 90.0f)

        viewBinding.cameraImage.viewTreeObserver.addOnGlobalLayoutListener {
            image = Bitmap.createScaledBitmap(image, viewBinding.cameraImage.getWidth(), viewBinding.cameraImage.getHeight(), true)

            viewBinding.cameraImage.setImageBitmap(image)

            val image = FirebaseVisionImage.fromBitmap(image)

            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

            detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    processResultText(firebaseVisionText)
                }
                .addOnFailureListener {
                    Log.i("something", "failed")
                }

            viewBinding.findMeaningButton.setOnClickListener {
                /*var query ="https://www.dictionary.com/browse/" +  viewBinding.selectedWord.text.toString().lowercase()
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(query)
                    )
                )*/

                var intent = Intent(this, MeaningActivity::class.java)
                intent.putExtra("word", viewBinding.selectedWord.text.toString())
                startActivity(intent)
            }

            viewBinding.arButton.setOnClickListener {

                if(flag == 1){
                    var query = "https://arvr.google.com/scene-viewer/1.0?file=https://market-assets.fra1.cdn.digitaloceanspaces.com/market-assets/models/"+ selectedWord+ "/model.gltf"

                    val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
                    sceneViewerIntent.data =
                        Uri.parse(query)
                    sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
                    startActivity(sceneViewerIntent)
                }else{
                    Toast.makeText(this, "AR Model currently unavailable", Toast.LENGTH_SHORT).show()
                }

            }
        }

        viewBinding.cameraImage.setOnTouchListener { view, motionEvent ->
            var x = motionEvent.x
            var y = motionEvent.y
            var index = 0
            var flag2 = 0
            for (i in coords){
                if(x > i.left && x < i.right){
                    if(y > i.top && y < i.bottom){
                        viewBinding.selectedWord.text = words[index]
                        for(i in modelList){
                            if(i == (viewBinding.selectedWord.text.toString().lowercase())){
                                Log.i("somethign", "yes")
                                selectedWord = i
                                flag = 1
                                flag2 = 1
                                break
                            }
                        }
                        break
                    }
                }
                index++
            }
            if(flag2 == 0) {
                flag = 0

            }




            true
        }



    }




    @SuppressLint("ClickableViewAccessibility")
    private fun processResultText(resultText: FirebaseVisionText) {
        if (resultText.textBlocks.size == 0) {
            Log.i("something", "no text found")
            return
        }

        for (block in resultText.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox
            for (line in block.lines) {
                val lineText = line.text
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox
                for (element in line.elements) {
                    val elementText = element.text
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox
                    Utils().removeSpecialCharacter(elementText).let { words.add(it) }
                    if (elementFrame != null) {
                        coords.add(elementFrame)
                    }

                }
            }
        }




    }




    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        var matrix: Matrix = Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(
            source,
            0,
            0,
            source.getWidth(),
            source.getHeight(),
            matrix,
            true
        );
    }



}

