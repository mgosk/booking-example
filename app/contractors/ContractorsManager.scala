package contractors

import javax.inject.Inject

import auth.model.{Identity, User}
import contractors.model.{Contractor, ContractorId}
import core.model.State

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ContractorsManager @Inject()(contractorsRepository: ContractorsRepository) {

  def create(contractor: Contractor): Future[Contractor] =
    contractorsRepository.insert(contractor).map { i => i }

  def update(contractor: Contractor)(implicit identity: Identity): Future[Contractor] =
    contractorsRepository.update(contractor).map { i => i }

  def find(id: ContractorId)(implicit identity: Identity): Future[Option[Contractor]] =
    contractorsRepository.find(id).map { i => i }

  def find(sortKey: String, sortReverse: Boolean) (implicit identity: Identity): Future[Seq[Contractor]] =
    contractorsRepository.find(sortKey, sortReverse).map { i => i }

  def archive(contractor: Contractor)(implicit identity: Identity): Future[Contractor] =
    contractorsRepository.update(contractor.copy(state = State.Archived)).map { i => i }

  def findByTerm(term: String)(implicit identity: Identity): Future[Seq[Contractor]] =
    contractorsRepository.findByTerm(term).map { i => i }

}
