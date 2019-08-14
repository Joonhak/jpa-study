package io.joonak.account.repository;

import com.querydsl.jpa.JPQLQuery;
import io.joonak.account.domain.Account;
import io.joonak.account.domain.QAccount;
import io.joonak.account.dto.AccountSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class AccountCustomRepositoryImpl extends QuerydslRepositorySupport implements AccountCustomRepository {

    public AccountCustomRepositoryImpl() {
        super(Account.class);
    }

    @Override
    public Page<Account> search(AccountSearchType type, String value, Pageable pageable) {
        final var account = QAccount.account;
        final JPQLQuery<Account> query;

        switch (type) {
            case EMAIL:
                query = from(account)
                        .where(account.email.address.stringValue().likeIgnoreCase(value + "%"));
                break;
            case NAME:
                query = from(account)
                        .where(
                                account.firstName.stringValue().likeIgnoreCase(value + "%")
                                        .or(account.lastName.stringValue().likeIgnoreCase(value + "%"))
                        );

                break;
            case ALL:
                query = from(account).fetchAll();
                break;
            default:
                throw new IllegalArgumentException(type.toString());
        }
        final var accounts = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(accounts, pageable, query.fetchCount());
    }

    @Override
    public List<Account> findCurrentlyRegistered(int limit) {
        final var account = QAccount.account;
        return from(account)
                .limit(limit)
                .orderBy(account.createdAt.desc())
                .fetch();
    }

}
