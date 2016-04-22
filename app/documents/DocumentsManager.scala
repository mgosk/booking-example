package documents

import javax.inject.Inject

import documents.model.Document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DocumentsManager @Inject() (documentsRepository: DocumentsRepository) {

  def create(document: Document): Future[Document] =
    documentsRepository.insert(document).map { i => i }

  def find(accessCode: String): Future[Option[Document]] =
    documentsRepository.find(accessCode).map { i => i }

}
