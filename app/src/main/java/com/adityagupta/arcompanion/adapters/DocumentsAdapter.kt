import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adityagupta.arcompanion.activities.EReaderActivity
import com.adityagupta.arcompanion.activities.PdfViewerActivity
import com.adityagupta.arcompanion.databinding.DocumentListItemBinding
import com.adityagupta.data.local.entities.Document

class DocumentsAdapter(
    private val context: Context,
    private val documentList: List<Document>,
    private val onItemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<DocumentsAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(document: Document)
    }

    // Describes an item view and its place within the RecyclerView
    class MyViewHolder(private val binding: DocumentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context,document: Document, onItemClickListener: OnItemClickListener?) {
            binding.documentTitleTv.text = document.title
            binding.documentSubjectTv.text = document.subject
            binding.documentCreationTv.text = document.creationDate
            binding.documentAuthorTv.text = document.author

            // Set click listener for the item view
            binding.liDocumentCard.setOnClickListener {
                onItemClickListener?.onItemClick(document)
                val intent = Intent(context, EReaderActivity::class.java );
                intent.putExtra("selectedDocumentUri", Uri.parse(document.docLocalUri))
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.i("pdfDebug", Uri.parse(document.docLocalUri).toString())
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            DocumentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    override fun onBindViewHolder( holder: MyViewHolder, position: Int) {
        holder.bind(context, documentList[position], onItemClickListener)
    }
}
