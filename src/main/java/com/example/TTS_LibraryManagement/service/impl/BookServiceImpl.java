package com.example.TTS_LibraryManagement.service.impl;

import com.example.TTS_LibraryManagement.dto.request.Book.BookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookSearchRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.ErrorRecordBook;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.BookCategoryResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.BookResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.ImportBooksResult;
import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.Category;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.enums.ErrorCode;
import com.example.TTS_LibraryManagement.mapper.BookMapper;
import com.example.TTS_LibraryManagement.repository.BookRepo;
import com.example.TTS_LibraryManagement.repository.CategoryRepo;
import com.example.TTS_LibraryManagement.service.BookService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookServiceImpl implements BookService {
    BookRepo bookRepo;
    BookMapper bookMapper;
    CategoryRepo categoryRepo;
    @Transactional
    public BookResponse createBook(BookCreationRequest request) {
        if (bookRepo.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.BOOK_EXISTED);
        }
        Book book = bookMapper.toBookCreate(request);
        if (book.getCategories() == null) {
            book.setCategories(new HashSet<>());
        }
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepo.findAllByIdInAndIsDeletedFalse(request.getCategoryIds());
            if (categories.size() != request.getCategoryIds().size()) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }
            book.setCategories(new HashSet<>(categories));
        }
        book.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        BookResponse bookResponse = bookMapper.toBookResponse(bookRepo.save(book));
        bookResponse.setCategories(book.getCategories().stream().map(bookMapper::toBookCategoryResponse).collect(Collectors.toList()));
        return bookResponse;
    }

    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        Book book = bookRepo.findBookByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        BookUpdateRequest currentDto = bookMapper.toBookUpdateRequest(book);
        Set<Long> currentCategoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        if (currentDto.equals(request) && currentCategoryIds.equals(request.getCategoryIds())) {
            throw new AppException(ErrorCode.BOOK_NOT_CHANGED);
        }
        bookMapper.toBookUpdate(book, request);
        List<Category> categories = categoryRepo.findAllByIdInAndIsDeletedFalse(request.getCategoryIds());
        if(categories.size() != request.getCategoryIds().size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        book.setCategories(new HashSet<>(categories));
        book.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        BookResponse bookResponse = bookMapper.toBookResponse(bookRepo.save(book));
        bookResponse.setCategories(book.getCategories().stream().map(bookMapper::toBookCategoryResponse).collect(Collectors.toList()));
        return bookMapper.toBookResponse(bookRepo.save(book));
    }

    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepo.findBookByIdAndIsDeletedFalse(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        book.setIsDeleted(1);
        book.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        bookRepo.save(book);
    }

    @Transactional
    public void restoreBook(Long bookId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if (book.getIsDeleted() == 0) {
            throw new AppException(ErrorCode.BOOK_NOT_DELETED);
        }
        book.setIsDeleted(0);
        book.setDeletedAt(null);
        book.setDeletedBy(null);
        bookRepo.save(book);
    }

    @Transactional
    public List<BookResponse> getBooks() {
        List<Book> books = bookRepo.findAllByIsDeletedFalse();
        if (books.isEmpty()) {
            throw new AppException(ErrorCode.BOOK_NOT_FOUND);
        }
        return books.stream().map(book -> {
            BookResponse bookResponse = bookMapper.toBookResponse(book);
            List<Category> categories = categoryRepo.findCategoriesByBookIdAndIsDeletedFalse(book.getId());
            List<BookCategoryResponse> categoryResponses = categories.stream().map(bookMapper::toBookCategoryResponse).collect(Collectors.toList());
            bookResponse.setCategories(categoryResponses);
            return bookResponse;
        }).collect(Collectors.toList());
    }

    @Transactional
    public BookResponse getBookById(Long id) {
        Book book = bookRepo.findBookByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        return bookMapper.toBookResponse(book);
    }

    @Transactional
    public Page<BookResponse> getBooksByPage(int page, int size, BookSearchRequest request) {
        if (page < 1) {
            throw new AppException(ErrorCode.PAGE_NO_ERROR);
        }
        if (size < 1) {
            throw new AppException(ErrorCode.PAGE_SIZE_ERROR);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "title"));
        Specification<Book> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), 0));
            if (request != null) {
                if (request.getCode() != null && !request.getCode().isEmpty()) {
                    predicates.add(cb.equal(cb.lower(root.get("code")), request.getCode()));
                }
                if (request.getAuthor() != null && !request.getAuthor().isEmpty()) {
                    predicates.add(cb.equal(root.get("author"), "%" + request.getAuthor().toLowerCase() + "%"));
                }
                if (request.getTitle() != null && !request.getTitle().isEmpty()) {
                    predicates.add(cb.equal(root.get("title"), "%" + request.getTitle().toLowerCase() + "%"));
                }
                if (request.getPublisher() != null && !request.getPublisher().isEmpty()) {
                    predicates.add(cb.equal(root.get("publisher"), "%" + request.getPublisher().toLowerCase() + "%"));
                }
                if (request.getPageCount() > 0) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("pageCount"), request.getPageCount()));
                }
                if (request.getPrintType() != null && !request.getPrintType().isEmpty()) {
                    predicates.add(cb.equal(cb.lower(root.get("printType")), request.getPrintType()));
                }
                if (request.getLanguage() != null && !request.getLanguage().isEmpty()) {
                    predicates.add(cb.equal(cb.lower(root.get("language")), request.getLanguage()));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return bookRepo.findAll(spec, pageable).map(book -> {
            BookResponse bookResponse = bookMapper.toBookResponse(book);
            List<Category> categories = categoryRepo.findCategoriesByBookIdAndIsDeletedFalse(book.getId());
            List<BookCategoryResponse> categoryResponses = categories.stream().map(bookMapper::toBookCategoryResponse).collect(Collectors.toList());
            bookResponse.setCategories(categoryResponses);
            return bookResponse;
        });
    }

//    @Transactional
//    public ApiResponse<List<ErrorRecordBook>> importBooksFromExcel(final MultipartFile file) {
//        List<ErrorRecordBook> errorRecords = new ArrayList<>();
//        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
//            Sheet sheet = workbook.getSheetAt(0);
//            boolean isFirstRow = true;
//            int rowNum = 0;
//            for (Row row : sheet) {
//                rowNum++;
//                if (isFirstRow) { isFirstRow = false; continue; }
//
//                // Xử lý an toàn trước khi tạo BookCreationRequest
//                try {
//                    BookCreationRequest bookCreationRequest = BookCreationRequest.builder()
//                            .code(getCellValueAsString(row.getCell(0)))
//                            .title(getCellValueAsString(row.getCell(1)))
//                            .author(getCellValueAsString(row.getCell(2)))
//                            .publisher(getCellValueAsString(row.getCell(3)))
//                            .pageCount(getNumericCellValue(row.getCell(4), 0))
//                            .printType(getCellValueAsString(row.getCell(5)))
//                            .language(getCellValueAsString(row.getCell(6)))
//                            .description(getCellValueAsString(row.getCell(7)))
//                            .quantity(getNumericCellValue(row.getCell(8), 0))
//                            .build();
//
//                    String categoryIdsStr = getCellValueAsString(row.getCell(9));
//                    log.info("Category IDs string at row {}: {}", rowNum, categoryIdsStr);
//
//                    Set<Long> categoryIds = parseCategoryIds(categoryIdsStr);
//                    log.info("Parsed category ids at row {}: {}", rowNum, categoryIds);
//
//                    bookCreationRequest.setCategoryIds(categoryIds);
//                    log.info("Book creation request at row {}: {}", rowNum, bookCreationRequest.toString());
//                    createBook(bookCreationRequest);
//                } catch (AppException e) {
//                    log.error("Lỗi khi xử lý dòng {}: {}", rowNum, e.getMessage());
//                    errorRecords.add(new ErrorRecordBook(rowNum, e.getErrorCode().getCode(), e.getMessage()));
//                } catch (Exception e) {
//                    log.error("Lỗi bất ngờ khi xử lý dòng {}: {}", rowNum, e.getMessage());
//                    errorRecords.add(new ErrorRecordBook(rowNum, ErrorCode.UNCATEGORIZED_EXCEPTION.getCode(), e.getMessage()));
//                }
//            }
//        } catch (IOException e) {
//            log.error("Lỗi khi đọc file Excel", e);
//            throw new AppException(ErrorCode.FAILED_TO_IMPORT_EXCEL);
//        }
//        if (!errorRecords.isEmpty()) {
//            return ApiResponse.<List<ErrorRecordBook>>builder()
//                    .code("IMPORT_ERRORS")
//                    .message("Có lỗi xảy ra khi nhập sách từ file Excel")
//                    .result(errorRecords)
//                    .build();
//        }
//
//        return ApiResponse.<List<ErrorRecordBook>>builder()
//                .code("PASSED")
//                .message("Nhập sách thành công")
//                .result(null)
//                .build();
//    }


    @Transactional
    public ImportBooksResult importBooksFromExcel(final MultipartFile file) {
        List<BookResponse> successfulImports = new ArrayList<>();
        List<ErrorRecordBook> errorRecords = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;
            int rowNum = 0;

            for (Row row : sheet) {
                rowNum++;
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                try {
                    BookCreationRequest bookCreationRequest = BookCreationRequest.builder()
                            .code(getCellValueAsString(row.getCell(0)))
                            .title(getCellValueAsString(row.getCell(1)))
                            .author(getCellValueAsString(row.getCell(2)))
                            .publisher(getCellValueAsString(row.getCell(3)))
                            .pageCount(getNumericCellValue(row.getCell(4), 0))
                            .printType(getCellValueAsString(row.getCell(5)))
                            .language(getCellValueAsString(row.getCell(6)))
                            .description(getCellValueAsString(row.getCell(7)))
                            .quantity(getNumericCellValue(row.getCell(8), 0))
                            .build();

                    String categoryIdsStr = getCellValueAsString(row.getCell(9));
                    Set<Long> categoryIds = parseCategoryIds(categoryIdsStr);
                    bookCreationRequest.setCategoryIds(categoryIds);
                    BookResponse bookResponse = createBook(bookCreationRequest);
                    successfulImports.add(bookResponse);
                } catch (AppException e) {
                    errorRecords.add(new ErrorRecordBook(rowNum,
                            e.getErrorCode() != null ? e.getErrorCode().getCode() : "UNKNOWN_ERROR",
                            e.getMessage()));
                } catch (Exception e) {
                    errorRecords.add(new ErrorRecordBook(rowNum,
                            ErrorCode.ROW_ERROR.getCode(),
                            e.getMessage()));
                }
            }
        } catch (IOException e) {
            errorRecords.add(new ErrorRecordBook(0, ErrorCode.FAILED_TO_IMPORT_EXCEL.getCode(), "Lỗi khi đọc Excel"));
        }

        return new ImportBooksResult(successfulImports, errorRecords);
    }


    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((int)cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception ex) {
                        return "";
                    }
                }
            default:
                return "";
        }
    }

    private Set<Long> parseCategoryIds(String categoryIdsStr) {
        if (categoryIdsStr == null || categoryIdsStr.trim().isEmpty()) {
            return new HashSet<>();
        }

        String[] idArray = categoryIdsStr.split("[,\\s]+");
        Set<Long> categoryIds = new HashSet<>();

        for (String id : idArray) {
            if (!id.trim().isEmpty()) {
                try {
                    categoryIds.add(Long.parseLong(id.trim()));
                } catch (NumberFormatException e) {
                    // Bỏ qua các giá trị không phải số
                    // Tùy vào yêu cầu, bạn có thể log lỗi hoặc xử lý khác
                }
            }
        }

        return categoryIds;
    }

    private int getNumericCellValue(Cell cell, int defaultValue) {
        if (cell == null) {
            return defaultValue;
        }

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return Integer.parseInt(cell.getStringCellValue().trim());
            }
        } catch (Exception e) {
            log.warn("Không thể chuyển đổi giá trị thành số: " + e.getMessage());
        }

        return defaultValue;
    }

    @Transactional
    public void exportBooksToExcel(HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Books");
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "ID", "Code", "Title", "Author", "Publisher",
                    "Page Count", "Print Type", "Language", "Description", "Quantity", "Categories"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            List<Book> books = bookRepo.findAllByIsDeletedFalse();
            int rowNum = 1;

            for (Book book : books) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(book.getId());
                row.createCell(1).setCellValue(book.getCode());
                row.createCell(2).setCellValue(book.getTitle());
                row.createCell(3).setCellValue(book.getAuthor());
                row.createCell(4).setCellValue(book.getPublisher());
                row.createCell(5).setCellValue(book.getPageCount());
                row.createCell(6).setCellValue(book.getPrintType());
                row.createCell(7).setCellValue(book.getLanguage());
                row.createCell(8).setCellValue(book.getDescription());
                row.createCell(9).setCellValue(book.getQuantity());
                String categoryIds = book.getCategories().stream()
                        .map(category -> String.valueOf(category.getCategoryName()))
                        .collect(Collectors.joining(", "));
                row.createCell(10).setCellValue(categoryIds);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.close();
        } catch (IOException e) {
            throw new AppException(ErrorCode.FAILED_TO_EXPORT_EXCEL);
        }
    }
}
