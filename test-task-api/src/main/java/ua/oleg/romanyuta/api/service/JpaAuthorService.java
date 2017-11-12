package ua.oleg.romanyuta.api.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.oleg.romanyuta.dao.AuthorRepository;
import ua.oleg.romanyuta.domain.Author;
import ua.oleg.romanyuta.api.exception.BadRequestException;
import ua.oleg.romanyuta.api.exception.NotFoundException;

import java.util.List;

@Service
public class JpaAuthorService implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author getAuthor(Long id) {
        Author author = authorRepository.findOne(id);
        if (author == null) {
            throw new NotFoundException(String.format("Author with id %s does not exist", id));
        }

        return author;
    }

    @Override
    public List<Author> getAllAuthors() {
        return Lists.newArrayList(authorRepository.findAll());
    }

    @Override
    public Author createAuthor(Author author) {
        if (author.getId() != null) {
            throw new BadRequestException("Unable to create new Author, id is autogenerated");
        }
        authorRepository.save(author);
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        Long id = author.getId();
        if (id == null) {
            throw new BadRequestException("Unable to update, id must be provided");
        }
        Author authorFromDb = authorRepository.findOne(id);
        if (authorFromDb == null) {
            throw new NotFoundException(String.format("Author with id %s does not exist", id));
        }

        authorRepository.save(author);

        return author;
    }
}
