#!/usr/bin/env bash
set -euo pipefail

BUCKET="books-eda-covers-dev"
TABLE="book_list"
CSV="book-covers.csv"

while IFS=, read -r BOOK_ID FILE; do
  [[ "$BOOK_ID" == "bookId" ]] && continue   # skip header
  [[ -z "$BOOK_ID" || -z "$FILE" ]] && continue
  if [[ ! -f "$FILE" ]]; then
    echo "Missing file: $FILE for bookId=$BOOK_ID" >&2
    continue
  fi

  EXT="${FILE##*.}"
  KEY="covers/${BOOK_ID}/cover.${EXT}"
  CT="$(file --mime-type -b "$FILE")"

  echo "Uploading $FILE -> s3://$BUCKET/$KEY"
  aws s3api put-object --bucket "$BUCKET" --key "$KEY" --content-type "$CT" --body "$FILE"

  echo "Linking bookId=$BOOK_ID to $KEY"
  aws dynamodb update-item \
    --table-name "$TABLE" \
    --key "{\"bookId\":{\"S\":\"$BOOK_ID\"}}" \
    --update-expression "SET coverUrlKey = :k REMOVE coverUrl" \
    --expression-attribute-values "{\":k\":{\"S\":\"$KEY\"}}"

done < "$CSV"
