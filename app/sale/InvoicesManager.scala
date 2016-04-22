package sale

import javax.inject.Inject

import auth.model.Identity
import sale.model.{Invoice, InvoiceId}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InvoicesManager @Inject()(invoicesRepository: InvoicesRepository) {

  def create(invoice: Invoice): Future[Invoice] =
    invoicesRepository.insert(invoice).map { i => i }

  def update(invoice: Invoice)(implicit identity: Identity): Future[Invoice] =
    invoicesRepository.update(invoice).map { i => i }

  def find(id: InvoiceId)(implicit identity: Identity): Future[Option[Invoice]] =
    invoicesRepository.find(id).map { i => i }

  def find()(implicit identity: Identity): Future[Seq[Invoice]] =
    invoicesRepository.find().map { i => i }

  def findAnonymously(id: InvoiceId): Future[Option[Invoice]] =
    invoicesRepository.findAnonymously(id).map { i => i }

}
